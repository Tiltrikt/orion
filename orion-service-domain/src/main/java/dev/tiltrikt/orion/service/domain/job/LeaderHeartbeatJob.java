package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.LeaderHeartbeatPublisher;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaderHeartbeatJob implements Runnable {

    @NotNull LeaderHeartbeatPublisher leaderHeartbeatPublisher;

    @NotNull OrionServiceNode thisOrionServiceNode;

    @Async
    @Override
    public void run() {
        leaderHeartbeatPublisher.publish(
                new LeaderHeartbeatEvent(
                        thisOrionServiceNode.getId(),
                        thisOrionServiceNode.getTerm()
                )
        );
    }
}
