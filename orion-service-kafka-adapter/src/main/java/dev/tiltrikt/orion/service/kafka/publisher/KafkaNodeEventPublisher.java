package dev.tiltrikt.orion.service.kafka.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.NodeEventPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaNodeEventPublisher implements NodeEventPublisher {

    @NotNull KafkaTemplate<String, NodeHeartbeatEvent> kafkaTemplate;

    @Override
    public void publishUpdate(@NotNull NodeHeartbeatEvent event) {
        kafkaTemplate.send(KafkaTopicConfiguration.NODE_REGISTRY_TOPIC, String.valueOf(event.getId()), event);
    }
}
