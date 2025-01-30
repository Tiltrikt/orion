package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerLeadershipManager;
import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.job.manager.LeaseExpirationCheckJobManager;
import dev.tiltrikt.orion.service.domain.leadership.LeadershipManager;
import dev.tiltrikt.orion.service.domain.leadership.LeadershipManagerImpl;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import dev.tiltrikt.orion.service.kafka.consumer.manager.KafkaConsumerLeadershipManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class LeadershipManagerConfiguration {

    @Bean
    @NotNull LeaseExpirationCheckJobManager leaseExpirationCheckJobManager(
            @NotNull TaskScheduler taskScheduler,
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher,
            @NotNull InstanceService instanceService
    ) {
        return new LeaseExpirationCheckJobManager(
                taskScheduler,
                registryUpdatePublisher,
                replicationRegistryUpdatePublisher,
                instanceService
        );
    }

    @Bean
    @NotNull ConsumerLeadershipManager consumerLeadershipManager(
            @Qualifier("kafkaInstanceEventConsumerManager")
            @NotNull ConsumerManager instanceEventConsumerManager,
            @Qualifier("kafkaReplicationConsumerManager")
            @NotNull ConsumerManager replicationConsumerManager
    ) {
        return new KafkaConsumerLeadershipManager(instanceEventConsumerManager, replicationConsumerManager);
    }

    @Bean
    @NotNull LeadershipManager leadershipManager(
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull ApplicationEventPublisher applicationEventPublisher,
            @NotNull NodeService nodeService
    ) {
        return new LeadershipManagerImpl(
                thisOrionServiceNode,
                applicationEventPublisher,
                nodeService
        );
    }
}
