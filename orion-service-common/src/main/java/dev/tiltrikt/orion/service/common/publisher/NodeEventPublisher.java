package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import org.jetbrains.annotations.NotNull;

public interface NodeEventPublisher {

    void publishUpdate(@NotNull NodeHeartbeatEvent event);
}
