package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.repository.InstanceRepository;
import dev.tiltrikt.orion.service.domain.repository.NodeRepository;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import dev.tiltrikt.orion.service.domain.service.InstanceServiceImpl;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import dev.tiltrikt.orion.service.domain.service.NodeServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    @NotNull InstanceService instanceService(
            @NotNull InstanceRepository instanceRepository,
            @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher) {
        return new InstanceServiceImpl(instanceRepository, replicationRegistryUpdatePublisher);
    }

    @Bean
    @NotNull NodeService nodeService(@NotNull NodeRepository repository) {
        return new NodeServiceImpl(repository);
    }
}
