package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.common.event.ReplicationRegistryUpdateEvent;
import org.jetbrains.annotations.NotNull;

public interface ReplicationRegistryUpdatePublisher {

    void publishUpdate(@NotNull String instanceId, @NotNull ReplicationRegistryUpdateEvent event);
}
