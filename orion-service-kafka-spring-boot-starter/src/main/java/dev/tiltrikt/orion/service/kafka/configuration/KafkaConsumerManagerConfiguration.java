package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.publisher.HeartbeatErrorPublisher;
import dev.tiltrikt.orion.service.domain.exception.InstanceNotFoundException;
import dev.tiltrikt.orion.service.domain.handler.follower.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.raft.CandidateRequestHandler;
import dev.tiltrikt.orion.service.domain.handler.raft.LeaderHeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.raft.VoteRequestHandler;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import dev.tiltrikt.orion.service.kafka.consumer.manager.KafkaInstanceEventConsumerManager;
import dev.tiltrikt.orion.service.kafka.consumer.manager.KafkaNodeEventConsumerManager;
import dev.tiltrikt.orion.service.kafka.consumer.manager.KafkaReplicationConsumerManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerManagerConfiguration {

    @Bean
    @NotNull ConsumerManager kafkaInstanceEventConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
            @NotNull RegistrationHandler registrationHandler,
            @NotNull DeregistrationHandler deregistrationHandler,
            @NotNull HeartbeatHandler heartbeatHandler,
            @NotNull InstanceModelFactory instanceModelFactory
    ) {
        return new KafkaInstanceEventConsumerManager(
                kafkaListenerEndpointRegistry,
                kafkaListenerContainerFactory,
                registrationHandler,
                deregistrationHandler,
                heartbeatHandler,
                instanceModelFactory
        );
    }

    @Bean
    @NotNull ConsumerManager kafkaNodeEventConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull CandidateRequestHandler candidateRequestHandler,
            @NotNull LeaderHeartbeatHandler leaderHeartbeatHandler,
            @NotNull VoteRequestHandler voteRequestHandler
    ) {
        return new KafkaNodeEventConsumerManager(
                kafkaListenerEndpointRegistry,
                kafkaListenerContainerFactory,
                candidateRequestHandler,
                leaderHeartbeatHandler,
                voteRequestHandler,
                thisOrionServiceNode
        );
    }

    @Bean
    @NotNull ConsumerManager kafkaReplicationConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull ReplicationHandler replicationHandler
    ) {
        return new KafkaReplicationConsumerManager(
                kafkaListenerEndpointRegistry,
                kafkaListenerContainerFactory,
                replicationHandler,
                thisOrionServiceNode
        );
    }

    @Bean
    @NotNull DefaultErrorHandler errorHandler(@NotNull HeartbeatErrorPublisher errorPublisher) {
        BackOff fixedBackOff = new FixedBackOff(1, 0);
        return new DefaultErrorHandler((consumerRecord, exception) -> {
            if (exception.getCause() instanceof InstanceNotFoundException) {
                errorPublisher.publishError(((InstanceHeartbeatEvent) consumerRecord.value()).getInstanceId());
            }
        }, fixedBackOff);
    }
}
