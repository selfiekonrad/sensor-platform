// Ad-hoc per-VU script: each VU registers one sensor, seeds a threshold, then
// sends a burst of readings. Kept working for quick experiments.
//
// For maintained tests prefer:
//   load_test.ts   ramping load with SLO thresholds (registration excluded from load)
//   smoke_test.ts  end-to-end incl. the read path
//
// Fixes vs. the original: removed an `Options#£` syntax error that made the file
// unparseable, the register helpers now return null instead of falling through
// to `undefined` (which crashed the VU), and the per-request console.log (which
// throttled throughput and measured logging rather than the API) is gated behind
// VERBOSE=1.

import { sleep, check } from 'k6';
import { Options } from 'k6/options';
import http from 'k6/http';
import { Sensor, SensorType } from './types.ts';

const register_url = 'http://sensor-platform.local:54101/api/register';
const threshold_url = 'http://sensor-platform.local:54101/api/threshold';
const ingest_url = 'http://sensor-platform.local:54101/api/sensors/data';
const sensor_send_amount = 100;

const TEMPERATURE_LOW = 10;
const TEMPERATURE_HIGH = 30;
const ANOMALY_PROBABILITY = 0.1;
const VERBOSE = __ENV.VERBOSE === '1';

export const options: Options = {
    vus: 70,
    iterations: 100,
    hosts: {
        'sensor-platform.local': '127.0.0.1',
    },
};

// One VU == one sensor. Register, seed a threshold, then stream readings.
export default () => {
    const sensor = register_temperature_sensor();
    if (sensor === null) {
        return;
    }
    seed_threshold(sensor, TEMPERATURE_LOW, TEMPERATURE_HIGH);

    for (let i = 0; i < sensor_send_amount; i++) {
        send_random_sensor_data(sensor);
        //sleep(1);
    }
};

function next_value(): number {
    if (Math.random() < ANOMALY_PROBABILITY) {
        return Math.random() < 0.5
            ? TEMPERATURE_LOW - (1 + Math.random() * 5)
            : TEMPERATURE_HIGH + (1 + Math.random() * 5);
    }
    return TEMPERATURE_LOW + Math.random() * (TEMPERATURE_HIGH - TEMPERATURE_LOW);
}

function seed_threshold(sensor: Sensor, low: number, high: number) {
    const payload = JSON.stringify({
        sensorId: sensor.id,
        lowThreshold: low,
        highThreshold: high,
    });

    const res = http.post(threshold_url, payload, {
        headers: { 'Content-Type': 'application/json' },
    });

    check(res, { "threshold seeded": (r) => r.status === 200 });

    if (res.status !== 200 && VERBOSE) {
        console.log(`Failed to seed threshold for sensor ${sensor.id}, status: ${res.status}`);
    }
}

function send_random_sensor_data(sensor: Sensor) {
    const payload = JSON.stringify({
        sensorId: sensor.id,
        sensorType: sensor.sensorType,
        value: next_value(),
        unit: "CELSIUS",
        timestamp: new Date().toISOString()
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'X-SDS-API-Key': sensor.apiKey,
        }
    }

    const res = http.post(ingest_url, payload, params);

    check(res, { "status is 202": (r) => r.status === 202 });

    if (res.status !== 202 && VERBOSE) {
        console.log(`Failed to send data for sensor ID: ${sensor.id}, status: ${res.status}`);
    }
}

export function register_pressure_sensor(): Sensor | null {
    return register_sensor(SensorType.PRESSURE);
}

export function register_temperature_sensor(): Sensor | null {
    return register_sensor(SensorType.TEMPERATURE);
}

function register_sensor(type: SensorType): Sensor | null {
    const sensor: Sensor = {
        id: null,
        name: type.toLowerCase() + "-sensor-" + Math.random(),
        apiKey: null,
        sensorType: type,
        createdAt: null,
    };

    const payload = JSON.stringify({ name: sensor.name, type: sensor.sensorType });
    const params = { headers: { 'Content-Type': 'application/json' } };

    const res = http.post(register_url, payload, params);
    check(res, { "status is 200": (r) => r.status === 200 });

    if (res.status !== 200) {
        if (VERBOSE) {
            console.log(`Failed to register ${type} sensor, status: ${res.status}`);
        }
        return null;
    }

    const responseData = JSON.parse(res.body as string);
    sensor.id = responseData.sensorId;
    sensor.apiKey = responseData.apiKey;
    sensor.createdAt = responseData.createdAt;
    return sensor;
}
