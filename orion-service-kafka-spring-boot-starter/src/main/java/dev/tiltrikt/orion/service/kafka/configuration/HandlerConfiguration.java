package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.handler.NodeHeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.NodeHeartbeatHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    @NotNull NodeHeartbeatHandler nodeHeartbeatHandler(@NotNull NodeService nodeService) {
        return new NodeHeartbeatHandlerImpl(nodeService);
    }
}
