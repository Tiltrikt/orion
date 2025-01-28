package dev.tiltrikt.orion.service.domain.job.manager;

import dev.tiltrikt.orion.service.common.event.BecomeFollowerEvent;
import dev.tiltrikt.orion.service.common.event.BecomeLeaderEvent;
import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.job.LeaseExpirationCheckJob;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaseExpirationCheckJobManager {

    @NotNull TaskScheduler taskScheduler;
    @NotNull Runnable leaseExpirationCheckJob;
    @NonFinal
    @Nullable ScheduledFuture<?> scheduledTask;

    public LeaseExpirationCheckJobManager(
            @NotNull TaskScheduler taskScheduler,
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher,
            @NotNull InstanceService instanceService) {
        this.taskScheduler = taskScheduler;
        this.leaseExpirationCheckJob = new LeaseExpirationCheckJob(
                registryUpdatePublisher,
                replicationRegistryUpdatePublisher,
                instanceService
        );
    }

    @EventListener
    public void becomeLeader(@NotNull BecomeLeaderEvent event) {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(leaseExpirationCheckJob, Duration.ofSeconds(10));
        }
    }

    @EventListener
    public void becomeFollower(@NotNull BecomeFollowerEvent event) {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
    }
}