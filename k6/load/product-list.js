import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    tags: {
        test_type: 'load',
    },
    stages: [
        {duration: '30s', target: 10},
        {duration: '30s', target: 50},
        {duration: '30s', target: 100},
        {duration: '30s', target: 0},
    ],

    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

export default function () {
    const response = http.get(
        'http://app:8080/products/list?categoryId=3&sort=newest'
    );

    check(response, {
        'status is 200': (r) => r.status === 200,
    });
    sleep(1);
}