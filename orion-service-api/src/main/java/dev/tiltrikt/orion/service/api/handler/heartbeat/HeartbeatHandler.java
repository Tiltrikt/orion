package dev.tiltrikt.orion.service.api.handler.heartbeat;

import org.jetbrains.annotations.NotNull;

public interface HeartbeatHandler {

    void update(@NotNull String instanceId);
}
