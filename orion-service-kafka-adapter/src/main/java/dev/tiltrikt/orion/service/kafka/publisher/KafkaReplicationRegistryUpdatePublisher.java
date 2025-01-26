package dev.tiltrikt.orion.service.kafka.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.ReplicationRegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.follower.model.Node;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaReplicationRegistryUpdatePublisher implements ReplicationRegistryUpdatePublisher {

    @NotNull KafkaTemplate<String, ReplicationRegistryUpdateEvent> kafkaTemplate;

    @NotNull Node thisNode;

    @Override
    public void publishUpdate(@NotNull String instanceId, @NotNull ReplicationRegistryUpdateEvent event) {
        if (thisNode.isLeader()) {
            kafkaTemplate.send(KafkaTopicConfiguration.REPLICATION_REGISTRY_TOPIC, instanceId, event);
        }
    }
}