package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.common.event.ReplicationEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReplicationRegistryUpdatePublisher {

    void publishUpdate(@NotNull String instanceId, @Nullable ReplicationEvent event);
}
