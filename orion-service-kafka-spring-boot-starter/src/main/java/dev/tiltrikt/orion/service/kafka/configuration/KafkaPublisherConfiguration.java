package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import dev.tiltrikt.orion.service.common.event.VoteEvent;
import dev.tiltrikt.orion.service.common.publisher.*;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.kafka.publisher.*;
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
    @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher(
            @NotNull KafkaTemplate<String, ReplicationEvent> kafkaTemplate,
            @NotNull OrionServiceNode thisOrionServiceNode
    ) {
        return new KafkaReplicationRegistryUpdatePublisher(kafkaTemplate, thisOrionServiceNode);
    }

    @Bean
    @NotNull VotePublisher votePublisher(
            @NotNull KafkaTemplate<String, VoteEvent> kafkaTemplate,
            @NotNull OrionServiceNode thisOrionServiceNode) {
        return new KafkaVotePublisher(kafkaTemplate, thisOrionServiceNode);
    }

    @Bean
    @NotNull LeaderHeartbeatPublisher leaderHeartbeatPublisher(
            @NotNull KafkaTemplate<String, LeaderHeartbeatEvent> kafkaTemplate,
            @NotNull OrionServiceNode thisOrionServiceNode
    ) {
        return new KafkaLeaderHeartbeatPublisher(kafkaTemplate, thisOrionServiceNode);
    }

    @Bean
    CandidateRequestPublisher candidateRequestPublisher(
            @NotNull KafkaTemplate<String, CandidateRequestEvent> kafkaTemplate,
            @NotNull OrionServiceNode thisOrionServiceNode
    ) {
        return new KafkaCandidateRequestPublisher(kafkaTemplate, thisOrionServiceNode);
    }
}
