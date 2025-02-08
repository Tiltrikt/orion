package dev.tiltrikt.orion.test.application;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Properties;

public class KafkaListenerTest {

    private static final String TOPIC = "fetch-registry";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";  // Адрес вашего Kafka брокера

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            String groupId = "group-" + i;  // Уникальная группа для каждого слушателя
            startConsumer(groupId);
        }

        Thread.sleep(100000);
    }

    public static void startConsumer(String groupId) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        new Thread(() -> {
            try {
                consumer.subscribe(java.util.Collections.singletonList(TOPIC));
                while (true) {
                    consumer.poll(1000).forEach(record -> {
                        System.out.println("Consumer " + groupId + " received message: " + record.value());
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
