package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.VotePublisher;
import dev.tiltrikt.orion.service.domain.handler.follower.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.handler.follower.ReplicationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.raft.*;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import dev.tiltrikt.orion.service.domain.service.VoteCounterService;
import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.task.manager.ElectionTimeoutTaskManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;

@Configuration
public class HandlerConfiguration {

    @Bean
    @NotNull RegistrationHandler registrationHandler(
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull InstanceService instanceService) {
        return new RegistrationHandlerImpl(instanceService, registryUpdatePublisher);
    }

    @Bean
    @NotNull DeregistrationHandler deregistrationHandler(
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull InstanceService instanceService) {
        return new DeregistrationHandlerImpl(instanceService, registryUpdatePublisher);
    }

    @Bean
    @NotNull HeartbeatHandler heartbeatHandler(
            @NotNull InstanceService instanceService,
            @NotNull InstanceModelFactory instanceModelFactory) {
        return new HeartbeatHandlerImpl(instanceService, instanceModelFactory);
    }

    @Bean
    @NotNull ReplicationHandler replicationHandler(@NotNull InstanceService instanceService) {
        return new ReplicationHandlerImpl(instanceService);
    }

    @Bean
    @NotNull VoteRequestHandler voteRequestHandler(
            @NotNull VoteCounterService voteCounterService,
            @NotNull StateMachine<States, Events> stateMachine,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull OrionServiceConfigurationProperties properties) {
        return new VoteRequestHandlerImpl(
                voteCounterService,
                stateMachine,
                thisOrionServiceNode,
                properties.getClusterSize()
        );
    }

    @Bean
    @NotNull CandidateRequestHandler candidateRequestHandler(
            @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager,
            @NotNull VotePublisher votePublisher,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull StateMachine<States, Events> stateMachine) {
        return new CandidateRequestHandlerImpl(
                electionTimeoutTaskManager,
                votePublisher,
                thisOrionServiceNode,
                stateMachine
        );
    }

    @Bean
    @NotNull LeaderHeartbeatHandler leaderHeartbeatHandler(
            @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager,
            @NotNull StateMachine<States, Events> stateMachine,
            @NotNull OrionServiceNode thisOrionServiceNode) {
        return new LeaderHeartbeatHandlerImpl(
                electionTimeoutTaskManager,
                stateMachine,
                thisOrionServiceNode
        );
    }
}
