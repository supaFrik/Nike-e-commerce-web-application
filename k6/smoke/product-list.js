import http from 'k6/http';
import {check} from 'k6';

export const options = {
    tags: {
        test_type: 'smoke',
    },
    vus: 1,
    duration: '30s',
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

export default function (){
    // Send GET request to API (docker environment)
    const response = http.get(
        'http://app:8080/products/list?categoryId=3&sort=newest'
    );

    check(response,{
        'status is 200': (r) => r.status === 200,
    });
}