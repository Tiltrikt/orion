package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.common.publisher.NodeEventPublisher;
import dev.tiltrikt.orion.service.domain.job.NodeHeartbeatJob;
import dev.tiltrikt.orion.service.domain.job.NodeLeaseExpirationCheckJob;
import dev.tiltrikt.orion.service.domain.leadership.LeadershipManager;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class JobConfiguration {

    @Bean
    @NotNull NodeHeartbeatJob nodeHeartbeatJob(
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull NodeEventPublisher nodeEventPublisher
    ) {
        return new NodeHeartbeatJob(thisOrionServiceNode, nodeEventPublisher);
    }

    @Bean
    @NotNull NodeLeaseExpirationCheckJob nodeLeaseExpirationCheckJob(
            @NotNull NodeService nodeService,
            @NotNull LeadershipManager leadershipManager
    ) {
        return new NodeLeaseExpirationCheckJob(nodeService, leadershipManager);
    }
}
