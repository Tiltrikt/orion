package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.common.publisher.HeartbeatErrorPublisher;
import dev.tiltrikt.orion.service.common.publisher.NodeEventPublisher;
import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaHeartbeatErrorPublisher;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaNodeEventPublisher;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaReplicationRegistryUpdatePublisher;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaPublisherConfiguration {

    @Bean
    @NotNull RegistryUpdatePublisher eventPublisher(@NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate) {
        return new KafkaRegistryUpdatePublisher(kafkaTemplate);
    }

    @Bean
    @NotNull HeartbeatErrorPublisher heartbeatErrorPublisher(@NotNull KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaHeartbeatErrorPublisher(kafkaTemplate);
    }

    @Bean
    @NotNull NodeEventPublisher nodeEventPublisher(@NotNull KafkaTemplate<String, NodeHeartbeatEvent> kafkaTemplate) {
        return new KafkaNodeEventPublisher(kafkaTemplate);
    }

    @Bean
    @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher(
            @NotNull KafkaTemplate<String, ReplicationEvent> kafkaTemplate,
            @NotNull OrionServiceNode thisOrionServiceNode
    ) {
        return new KafkaReplicationRegistryUpdatePublisher(kafkaTemplate, thisOrionServiceNode);
    }
}
