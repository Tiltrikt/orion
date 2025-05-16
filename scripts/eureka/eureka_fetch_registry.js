import { sleep } from "k6";
import http from 'k6/http';
import { check } from "k6";

const baseUrl = "http://host.docker.internal:8761/eureka/v2/apps";

function generateInstanceXML(instanceId) {
    return `
    <instance>
        <hostName>ec2-203-0-113-25.compute-1.amazonaws.com</hostName>
        <app>myApp</app>
        <ipAddr>203.0.113.25</ipAddr>
        <status>UP</status>
        <port>8080</port>
        <dataCenterInfo>
            <name>Amazon</name>
            <metadata>
                <instance-id>${instanceId}</instance-id>
            </metadata>
        </dataCenterInfo>
        <leaseInfo>
            <evictionDurationInSecs>30</evictionDurationInSecs>
        </leaseInfo>
    </instance>
    `;
}

export const options = {
    scenarios: {
        reader_scenario: {
            executor: "ramping-vus",
            startVUs: 100,
            stages: [
                { duration: "1s", target: 10000 },
                { duration: "1m", target: 10000 },
            ],
            exec: "reader_fun",
        },
    },
};

function fetch_registry(instanceId) {
    const instanceXML = generateInstanceXML(instanceId);
    const url = `${baseUrl}`;

    const response = http.get(url);

    check(response, {
        "status is 200": (r) => r.status === 200,
    });

}

export function reader_fun() {
    if (__ITER === 0) {
        let randomSleep = Math.random();
        sleep(randomSleep);
    }
    fetch_registry()
    sleep(1)
}