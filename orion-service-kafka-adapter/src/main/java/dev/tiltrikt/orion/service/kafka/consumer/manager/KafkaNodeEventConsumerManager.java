package dev.tiltrikt.orion.service.kafka.consumer.manager;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.service.domain.handler.NodeHeartbeatHandler;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.kafka.consumer.KafkaNodeEventConsumer;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class KafkaNodeEventConsumerManager extends KafkaAbstractConsumerManager {

    private final static String KAFKA_NODE_EVENT_CONSUMER = "kafka-node-event-consumer";

    @NotNull NodeHeartbeatHandler nodeHeartbeatHandler;

    public KafkaNodeEventConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory kafkaListenerContainerFactory,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull NodeHeartbeatHandler nodeHeartbeatHandler) {
        super(kafkaListenerEndpointRegistry, kafkaListenerContainerFactory, thisOrionServiceNode);
        this.nodeHeartbeatHandler = nodeHeartbeatHandler;
    }

    @Override
    @SneakyThrows
    public void startListening() {
        MethodKafkaListenerEndpoint<String, NodeHeartbeatEvent> kafkaListenerEndpoint = createDefaultMethodKafkaListenerEndpoint(
                KafkaTopicConfiguration.NODE_EVENT_TOPIC,
                KAFKA_NODE_EVENT_CONSUMER,
                String.valueOf(thisOrionServiceNode.getId())
        );
        kafkaListenerEndpoint.setBean(new KafkaNodeEventConsumer(nodeHeartbeatHandler));
        kafkaListenerEndpoint.setMethod(KafkaNodeEventConsumer.class.getMethod("onMessage", ConsumerRecord.class));
        kafkaListenerEndpointRegistry.registerListenerContainer(kafkaListenerEndpoint, kafkaListenerContainerFactory, true);
    }

    @Override
    public void stopListening() {
        destroyContainer(KAFKA_NODE_EVENT_CONSUMER);
    }
}
