package dev.tiltrikt.orion.service.domain.job.manager;

import dev.tiltrikt.orion.service.common.publisher.LeaderHeartbeatPublisher;
import dev.tiltrikt.orion.service.domain.job.LeaderHeartbeatJob;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaderHeartbeatJobManager {

    @NotNull TaskScheduler taskScheduler;

    @NotNull Runnable leaderHeartbeatJob;

    @NonFinal
    @Nullable ScheduledFuture<?> scheduledTask;

    public LeaderHeartbeatJobManager(
            @NotNull TaskScheduler taskScheduler,
            @NotNull LeaderHeartbeatPublisher leaderHeartbeatPublisher,
            @NotNull OrionServiceNode thisOrionServiceNode) {
        this.taskScheduler = taskScheduler;
        this.leaderHeartbeatJob = new LeaderHeartbeatJob(leaderHeartbeatPublisher, thisOrionServiceNode);
    }

    public void startJob() {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(
                    leaderHeartbeatJob,
                    Duration.ofSeconds(2)
            );
        }
    }

    public void stopJob() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
    }
}
