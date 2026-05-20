import http from 'k6/http';
import {sleep, check} from 'k6';

export const options = {
    iterations: 1,
};

export default function () {
    let res =
        http.post('http://localhost:3435/api/register')
            .body(
                JSON.stringify({
                    sensorRegistryData: {
                        name: 'k6-sensor',
                        type: 'e58ed763-928c-4155-bee9-fdbaaadc15f3',
                    }
                })
            )
            .header('Content-Type', 'application/json');

    check(res, { "status is 200": (res) => res.status === 200 });
    sleep(1);
}