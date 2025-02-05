import { sleep } from "k6";
import http from 'k6/http';// Убедитесь, что http импортируется здесь
import { check } from "k6";  // Можно добавить check для проверки ответов

const baseUrl = "http://host.docker.internal:8761/eureka/v2/apps/myApp";

// Функция для генерации XML с динамичным `instance-id`
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
                <instance-id>${instanceId}</instance-id>  <!-- Динамическое изменение instance-id -->
            </metadata>
        </dataCenterInfo>
        <leaseInfo>
            <evictionDurationInSecs>30000</evictionDurationInSecs>
        </leaseInfo>
    </instance>
    `;
}

export const options = {
    scenarios: {
        writer_scenario: {
            executor: "ramping-vus",
            startVUs: 100,
            stages: [
                { duration: "1m", target: 1000 },
                // { duration: "1m", target: 1000 },
            ],
            exec: "writer_fun",
        },
    },
};

function register(instanceId) {
    const instanceXML = generateInstanceXML(instanceId);  // Генерация XML с динамичным instance-id
    const url = `${baseUrl}`;
    const headers = {
        "Content-Type": "application/xml",
    };

    // Здесь вызываем http.post
    const response = http.post(url, instanceXML, { headers });

    check(response, {
        "status is 204": (r) => r.status === 204,
    });

    console.log(instanceId, Date.now())
}

function deregister(instanceId) {
    const url = `${baseUrl}/${instanceId}`;

    // Здесь вызываем http.del
    const response = http.del(url);

}

function heartbeat(instanceId) {
    const url = `${baseUrl}/${instanceId}`;

    // Здесь вызываем http.put
    const response = http.put(url);

}

export function writer_fun() {
    const instanceId = __VU;  // Используем виртуальный пользователь для динамического instanceId
    if (__ITER === 0) {
        register(instanceId);  // Регистрация инстанса
    }
    sleep(5);
    heartbeat(instanceId);  // Отправка heartbeat
}
