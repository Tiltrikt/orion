package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.common.event.ReplicationEvent;
import org.jetbrains.annotations.NotNull;

public interface ReplicationRegistryUpdatePublisher {

    void publishUpdate(@NotNull String instanceId, @NotNull ReplicationEvent event);
}
