package dev.tiltrikt.orion.client.kafka.consumer;

import dev.tiltrikt.orion.client.domain.model.OrionInstance;
import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;


@KafkaListener(
        topics = KafkaTopicConfiguration.HEARTBEAT_ERROR_TOPIC,
        groupId = "${spring.application.name}"
)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatErrorHandler implements ConsumerSeekAware {

    @NotNull OrionInstance thisOrionInstance;

    @NotNull ServiceRegistry<OrionInstance> orionServiceRegistry;

    @KafkaHandler
    public void receive(@Payload(required = false) String ignored, @Header(KafkaHeaders.RECEIVED_KEY) String instanceId) {
        if (thisOrionInstance.getInstanceId().equals(instanceId)) {
            orionServiceRegistry.register(thisOrionInstance);
        }
    }

    @Override
    public void onPartitionsAssigned(
            @NotNull Map<TopicPartition, Long> assignments,
            @NotNull ConsumerSeekAware.ConsumerSeekCallback callback
    ) {
        for (TopicPartition topicPartition : assignments.keySet()) {
            if (topicPartition.topic().equals(KafkaTopicConfiguration.HEARTBEAT_ERROR_TOPIC)) {
                callback.seekToEnd(topicPartition.topic(), topicPartition.partition());
            }
        }
    }
}
