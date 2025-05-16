import { sleep } from "k6";
import http from 'k6/http';
import { check } from "k6";

const consulBaseUrl = "http://host.docker.internal:8500/v1/agent/service";

function generateServiceJSON(serviceId) {
    return {
        ID: serviceId,
        Name: "myApp",
        Address: "203.0.113.25",
        Port: 8080,
        Tags: ["status:UP"],
        Check: {
            HTTP: `http://203.0.113.25:8080/health`,
            Interval: "10s",
            DeregisterCriticalServiceAfter: "30s",
        }
    };
}

export const options = {
    scenarios: {
        writer_scenario: {
            executor: "ramping-vus",
            startVUs: 100,
            stages: [
                { duration: "1m", target: 300 },
                { duration: "1m", target: 1000 },
            ],
            exec: "writer_fun",
        },
    },
};

function register(serviceId) {
    const serviceJSON = generateServiceJSON(serviceId);
    const url = `${consulBaseUrl}/register`;
    const headers = {
        "Content-Type": "application/json",
    };

    const response = http.put(url, JSON.stringify(serviceJSON), { headers });

    check(response, {
        "status is 200": (r) => r.status === 200,
    });

    console.log(serviceId, Date.now());
}

function deregister(serviceId) {
    const url = `${consulBaseUrl}/deregister/${serviceId}`;

    const response = http.put(url);

    check(response, {
        "status is 200": (r) => r.status === 200,
    });
}

function heartbeat(serviceId) {
    const url = `${consulBaseUrl}/check/pass/service:${serviceId}`;

    const response = http.put(url);

    check(response, {
        "status is 200": (r) => r.status === 200,
    });
}

export function writer_fun() {
    const serviceId = __VU.toString();
    if (__ITER === 0) {
        register(serviceId);
    }
    sleep(5);
    heartbeat(serviceId);
}
