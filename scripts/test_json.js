import {sleep} from "k6"
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

const connection = new Connection({
    address: "127.0.0.1:9092"
});

export const options = {
    vus: 3000, // Количество виртуальных пользователей
    duration: '1m', // Продолжительность теста 1 минута
    thresholds: {

        kafka_writer_error_count: ["count == 0"],
        kafka_reader_error_count: ["count == 0"],
    },
};

const schemaRegistry = new SchemaRegistry();

export default function () {
    writer.produce({
        messages: [
            {
                value: schemaRegistry.serialize({
                    data: {
                        "instanceId": "147.232.180.61:8084"
                    },
                    schemaType: SCHEMA_TYPE_JSON,
                }),
                headers: {
                    "__TypeId__": "dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent"
                },
            },
        ],
    });
    sleep(1);
}

cat ./scripts/test_json.js | docker run --rm -i --network="host" mostafamoradian/xk6-kafka:latest run -