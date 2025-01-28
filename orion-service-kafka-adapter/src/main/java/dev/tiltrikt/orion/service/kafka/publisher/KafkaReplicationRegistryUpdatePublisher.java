package dev.tiltrikt.orion.service.kafka.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaReplicationRegistryUpdatePublisher implements ReplicationRegistryUpdatePublisher {

    @NotNull KafkaTemplate<String, ReplicationEvent> kafkaTemplate;

    @NotNull OrionServiceNode thisOrionServiceNode;

    @Override
    public void publishUpdate(@NotNull String instanceId, @Nullable ReplicationEvent event) {
        if (thisOrionServiceNode.isLeader()) {
            System.out.println("Publishing replication update: " + event);
            kafkaTemplate.send(KafkaTopicConfiguration.REPLICATION_TOPIC, instanceId, event);
        }
    }
}