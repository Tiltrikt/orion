package dev.tiltrikt.orion.service.kafka.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.common.publisher.HeartbeatErrorPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaHeartbeatErrorPublisher implements HeartbeatErrorPublisher {

    @NotNull KafkaTemplate<String, String> kafkaTemplate;

    public void publishError(@NotNull String instanceId) {
        kafkaTemplate.send(KafkaTopicConfiguration.HEARTBEAT_ERROR_TOPIC, instanceId, "");
    }
}
