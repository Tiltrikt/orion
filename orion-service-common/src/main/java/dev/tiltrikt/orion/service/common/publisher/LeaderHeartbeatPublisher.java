package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import org.jetbrains.annotations.NotNull;

public interface LeaderHeartbeatPublisher {

    void publish(@NotNull LeaderHeartbeatEvent event);
}
