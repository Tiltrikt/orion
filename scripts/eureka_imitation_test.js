import {sleep} from "k6";
import {Reader, SCHEMA_TYPE_JSON, SchemaRegistry, Writer,} from "k6/x/kafka";

const writer = new Writer({
    brokers: ["127.0.0.1:9092"],
    topic: "instance-event",
});

const reader = new Reader({
    brokers: ["127.0.0.1:9092"],
    topic: "fetch-registry"
});

const schemaRegistry = new SchemaRegistry();

export const options = {
    scenarios: {
        writer_scenario: {
            executor: "ramping-vus",
            startVUs: 100,
            stages: [
                {duration: "1m", target: 300},
                {duration: "1m", target: 1000},
            ],
            exec: "writer_fun",
        },

        // reader_scenario: {
        //     executor: "constant-vus",
        //     vus: 2,
        //     duration: "20s",
        //     exec: "reader_fun",
        // },
    },
};

function register(instanceId) {
    writer.produce({
        messages: [
            {
                value: schemaRegistry.serialize({
                    data: {
                        "serviceId": "mock-orion-client",
                        "host": "localhost",
                        "port": instanceId,
                        "leaseDuration": 15,
                        "metadata": {},
                    },
                    schemaType: SCHEMA_TYPE_JSON,
                }),
                headers: {
                    "__TypeId__": "dev.tiltrikt.orion.common.event.InstanceRegistrationEvent",
                },
            },
        ],
    });
    console.log(instanceId, Date.now())
}

function deregister(instanceId) {
    writer.produce({
        messages: [
            {
                value: schemaRegistry.serialize({
                    data: {

                        "instanceId": "localhost:" + instanceId
                    },
                    schemaType: SCHEMA_TYPE_JSON,
                }),
                headers: {
                    "__TypeId__": "dev.tiltrikt.orion.common.event.InstanceDeregistrationEvent",
                },
            },
        ],
    });
}

function heartbeat(instanceId) {
    writer.produce({
        messages: [
            {
                value: schemaRegistry.serialize({
                    data: {
                        "instanceId": "localhost:" + instanceId
                    },
                    schemaType: SCHEMA_TYPE_JSON,
                }),
                headers: {
                    "__TypeId__": "dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent",
                },
            },
        ],
    });
}

export function writer_fun() {
    if (__ITER === 0) {
        register(__VU);
    }
    sleep(5);
    heartbeat(__VU);
}

export function reader_fun() {
    let messages = reader.consume({limit: 1});
    let deserializedValue = schemaRegistry.deserialize({
        data: messages[0].value,
        schemaType: SCHEMA_TYPE_JSON,
    });
    console.log(deserializedValue);
}