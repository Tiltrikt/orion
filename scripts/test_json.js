import { check, sleep } from "k6";
import {
    Writer,
    Reader,
    Connection,
    SchemaRegistry,
    SCHEMA_TYPE_JSON,
} from "k6/x/kafka";

const writer = new Writer({
    brokers: ["127.0.0.1:9092"],
    topic: "instance-event",
});

const reader = new Reader({
    brokers: ["127.0.0.1:9092"],
    topic: "instance-event"
});

const schemaRegistry = new SchemaRegistry();

export const options = {
    scenarios: {
        writer_scenario: {
            executor: "constant-vus",  // Используем постоянное количество виртуальных пользователей
            vus: 10,  // 1000 сервисов для записи
            duration: "1m",  // Продолжительность 1 минута
            exec: "writer_fun",  // Эта функция будет выполняться для всех виртуальных пользователей в потоке
        },

        // Поток для чтения
        reader_scenario: {
            executor: "constant-vus",  // Используем постоянное количество виртуальных пользователей
            vus: 1,  // 1000 сервисов для чтения
            duration: "1m",  // Продолжительность 1 минута
            exec: "reader_fun",  // Эта функция будет выполняться для всех виртуальных пользователей в потоке
        },
    },
};

export function writer_fun() {
    writer.produce({
        messages: [
            {
                value: schemaRegistry.serialize({
                    data: {
                        "instanceId": `147.232.180.61:8084`
                    },
                    schemaType: SCHEMA_TYPE_JSON,
                }),
                headers: {
                    "__TypeId__": "dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent",
                },
            },
        ],
    });

    sleep(1);
}

export function reader_fun() {
    let messages = reader.consume({ limit: 1 });
    let deserializedValue = schemaRegistry.deserialize({
        data: messages[0].value,
        schemaType: SCHEMA_TYPE_JSON,
    });
    console.log(deserializedValue);
    sleep(3);
}