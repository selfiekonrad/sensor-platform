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

export let options:Options#£ = {
    vus: 70,
    iterations: 100,
    hosts: {
        'sensor-platform.local': '127.0.0.1',
    },
};

// This function will be called for each VU (Virtual User) which
// represents one single sensor in the system and will send random sensor
// data to the platform.
export default () => {
    const sensor: Sensor = register_temperature_sensor();
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

    if (res.status !== 200) {
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

    const res = http.post(
        ingest_url,
        payload,
        params
    );

    check(res, { "status is 202": (r) => r.status === 202 });

    if (res.status === 202) {
        console.log(`Sent data for sensor ID: ${sensor.id}`);
    } else {
        console.log(`Failed to send data for sensor ID: ${sensor.id}, status: ${res.status}`);
    }
}

export function register_pressure_sensor(): Sensor {
    const sensor : Sensor = {
        id: null,
        name: "pressure-sensor-" + Math.random(),
        apiKey: null,
        sensorType: SensorType.PRESSURE,
        createdAt: null
    }

    const payload = JSON.stringify({
        name: sensor.name,
        type: sensor.sensorType
    });

    const params = {
        headers: {
            'Content-Type': 'application/json'
        }
    }

    const res = http.post(
        register_url,
        payload,
        params
    );

    check(res, { "status is 200": (r) => r.status === 200 });

    if (res.status === 200) {
        const responseData = JSON.parse(<string>res.body);
        console.log(`Registered sensor with ID: ${responseData.sensorId}`);
        sensor.id = responseData.sensorId;
        sensor.apiKey = responseData.apiKey;
        sensor.createdAt = responseData.createdAt;

        return sensor;
    }

    console.log(
        `sensorId: ${sensor.id}, name: ${sensor.name}, apiKey: ${sensor.apiKey}, sensorType: ${sensor.sensorType}, createdAt: ${sensor.createdAt}`
    );
}

export function register_temperature_sensor(): Sensor {
    const sensor : Sensor = {
        id: null,
        name: "temperature-sensor-" + Math.random(),
        apiKey: null,
        sensorType: SensorType.TEMPERATURE,
        createdAt: null
    }

    const payload = JSON.stringify({
        name: sensor.name,
        type: sensor.sensorType
    });

    const params = {
        headers: {
            'Content-Type': 'application/json'
        }
    }

    const res = http.post(
        register_url,
        payload,
        params
    );

    check(res, { "status is 200": (r) => r.status === 200 });

    if (res.status === 200) {
        const responseData = JSON.parse(<string>res.body);
        console.log(`Registered sensor with ID: ${responseData.sensorId}`);
        sensor.id = responseData.sensorId;
        sensor.apiKey = responseData.apiKey;
        sensor.createdAt = responseData.createdAt;

        return sensor;
    }

    console.log(
        `sensorId: ${sensor.id}, name: ${sensor.name}, apiKey: ${sensor.apiKey}, sensorType: ${sensor.sensorType}, createdAt: ${sensor.createdAt}`
    );
}