import http from 'k6/http';
import { check, sleep } from 'k6';
import {Trend} from "k6/metrics";

const productListTrend = new Trend("product_list_duration");

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
        'http://app:8080/products/list/data?categoryId=3&sort=newest'
    );

    productListTrend.add(response.timings.duration);

    check(response, {
        'status is 200': (r) => r.status === 200,
        'Body has content': (r) => {
            const body = JSON.parse(r.body);
            return body.content !== undefined && body.content.length > 0;
        },
        'Total elements > 0': (r) => {
            const totalElements = JSON.parse(r.body).totalElements;
            return totalElements > 0;
        }
    });
    sleep(1);
}