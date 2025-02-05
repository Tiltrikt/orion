import {Reader, SCHEMA_TYPE_JSON, SchemaRegistry, Writer} from "k6/x/kafka";
import {check} from "k6";

const reader = new Reader({
    brokers: ["127.0.0.1:9092"],
    topic: "fetch-registry"
});

const schemaRegistry = new SchemaRegistry();

export const options = {
    scenarios: {
        reader_scenario: {
            executor: "ramping-vus",
            startVUs: 10000,
            stages: [
                { duration: "1m", target: 10000 },
            ],
            exec: "reader_fun",
        },
    },
};

export function reader_fun() {
    let messages;

    // Проверка на наличие сообщений
    try {
        messages = reader.consume({limit: 1});
        check(messages, {
            "message exists": (msg) => msg.length > 0,
        });

        if (messages.length === 0) {
            throw new Error("No messages found.");
        }

        // Попытка десериализовать сообщение
        let deserializedValue = schemaRegistry.deserialize({
            data: messages[0].value,
            schemaType: SCHEMA_TYPE_JSON,
        });

        // Проверка успешности десериализации
        check(deserializedValue, {
            "deserialized value is not null": (value) => value !== null,
        });

        // Вывод успешного результата
        console.log(deserializedValue);
    } catch (error) {
        // Обработка ошибок
        console.error("Error in reader_fun:", error.message);
        check(false, { "error caught": (v) => v === false }); // Задать false для отображения ошибки в k6 отчетах
    }
}
