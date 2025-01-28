package dev.tiltrikt.orion.service.kafka.consumer.manager;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.model.Node;
import dev.tiltrikt.orion.service.kafka.consumer.KafkaReplicationConsumer;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaReplicationConsumerManager extends KafkaAbstractConsumerManager {

    private final static String KAFKA_REPLICATION_CONSUMER = "kafka-replication-consumer";

    @NotNull ReplicationHandler replicationHandler;

    public KafkaReplicationConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory kafkaListenerContainerFactory,
            @NotNull Node thisNode,
            @NotNull ReplicationHandler replicationHandler) {
        super(kafkaListenerEndpointRegistry, kafkaListenerContainerFactory, thisNode);
        this.replicationHandler = replicationHandler;
    }

    @Override
    @SneakyThrows
    public void startListening() {
        MethodKafkaListenerEndpoint<String, ReplicationEvent> kafkaListenerEndpoint = createDefaultMethodKafkaListenerEndpoint(
                KafkaTopicConfiguration.REPLICATION_TOPIC,
                KAFKA_REPLICATION_CONSUMER,
                String.valueOf(thisNode.getId())
        );
        kafkaListenerEndpoint.setBean(new KafkaReplicationConsumer(replicationHandler));
        kafkaListenerEndpoint.setMethod(KafkaReplicationConsumer.class.getMethod("onMessage", ConsumerRecord.class));
        kafkaListenerEndpointRegistry.registerListenerContainer(kafkaListenerEndpoint, kafkaListenerContainerFactory, true);
    }

    @Override
    public void stopListening() {
        destroyContainer(KAFKA_REPLICATION_CONSUMER);
    }
}
