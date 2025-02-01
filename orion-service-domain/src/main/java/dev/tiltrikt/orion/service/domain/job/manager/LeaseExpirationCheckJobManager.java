package dev.tiltrikt.orion.service.domain.job.manager;

import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.job.LeaseExpirationCheckJob;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

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

    public void startJob() {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(
                    leaseExpirationCheckJob,
                    Instant.now().plusSeconds(10),
                    Duration.ofSeconds(10)
            );
        }
    }

    public void stopJob() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
    }
}