package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.LeaderHeartbeatPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaderHeartbeatJob implements Runnable {

    @NotNull LeaderHeartbeatPublisher leaderHeartbeatPublisher;

    @Override
    public void run() {
        leaderHeartbeatPublisher.publish(new LeaderHeartbeatEvent());
    }
}
