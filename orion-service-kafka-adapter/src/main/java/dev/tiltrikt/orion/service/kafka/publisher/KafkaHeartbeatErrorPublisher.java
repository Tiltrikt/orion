package dev.tiltrikt.orion.service.kafka.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaHeartbeatErrorPublisher {

    @NotNull KafkaTemplate<String, String> kafkaTemplate;

    public void publishError(@NotNull String instanceId) {
        kafkaTemplate.send(KafkaTopicConfiguration.HEARTBEAT_ERROR_TOPIC, instanceId, "");
    }
}
