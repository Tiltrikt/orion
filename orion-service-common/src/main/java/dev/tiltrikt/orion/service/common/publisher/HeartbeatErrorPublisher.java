package dev.tiltrikt.orion.service.common.publisher;

import org.jetbrains.annotations.NotNull;

public interface HeartbeatErrorPublisher {

    public void publishError(@NotNull String instanceId);
}
