package dev.tiltrikt.orion.service.domain.handler.leader.heartbeat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface HeartbeatHandler {

    void update(@NotNull String instanceId);

    void updateBatch(@NotNull List<String> instanceIdList);
}
