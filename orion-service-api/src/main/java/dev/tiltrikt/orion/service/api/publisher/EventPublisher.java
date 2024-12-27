package dev.tiltrikt.orion.service.api.publisher;

import dev.tiltrikt.orion.service.api.model.InstanceModel;
import org.jetbrains.annotations.NotNull;

public interface EventPublisher {

    void publishRegistration(@NotNull InstanceModel instance);

    void publishDeregistration(@NotNull String instanceId);
}
