package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import org.jetbrains.annotations.NotNull;

public interface RegistryUpdatePublisher {

    void publishRegistration(@NotNull String instanceId, @NotNull RegistryUpdateEvent event);

    void publishDeregistration(@NotNull String instanceId);
}
