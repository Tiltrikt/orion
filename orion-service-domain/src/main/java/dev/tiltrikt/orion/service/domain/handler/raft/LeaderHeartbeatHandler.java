package dev.tiltrikt.orion.service.domain.handler.raft;

import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import org.jetbrains.annotations.NotNull;

public interface LeaderHeartbeatHandler {

    void handle(@NotNull LeaderHeartbeatEvent event);
}
