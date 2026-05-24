import { sleep, check } from 'k6';
import { Options } from 'k6/options';
import http from 'k6/http';

export let options:Options = {
    vus: 50,
    duration: '10s',
    iterations: 50,
    hosts: {
        'sensor-platform.local': '127.0.0.1',
    },
};

export default () => {
    register_sensors();
};

export function register_sensors() {
    const payload = JSON.stringify({
        name: "temperature-sensor-" + Math.random(),
        type: "TEMPERATURE"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json'
        }
    }

    const res = http.post(
        'http://sensor-platform.local:54101/api/register',
        payload,
        params
    );

    if (res.status === 200) {
        console.log('ros.body: ' + res.body);
        const responseData = JSON.parse(res.body);
        console.log(`Registered sensor with ID: ${responseData.sensorId}`);
    }
}

export function test() {
    const payload = JSON.stringify({
        sensorId: "8b6106bb-917e-4593-88f2-d9159ab18228",
        sensorType: "TEMPERATURE",
        value: 23.5,
        unit: "CELSIUS",
        timestamp: "2026-05-20T12:00:00.000Z"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'X-SDS-API-Key': 'exQI1Kf8WVqqlqbR30Vf3frYrwR_NV7o6xxgV4y7t5A',
        },
    };

    const res = http.post(
        'http://sensor-platform.local:54101/api/sensors/data',
        payload,
        params
    );

    check(res, { "status is 202": (r) => r.status === 202 });
    sleep(1);
}