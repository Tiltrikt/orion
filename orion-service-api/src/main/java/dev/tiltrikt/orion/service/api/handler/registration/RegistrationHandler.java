package dev.tiltrikt.orion.service.api.handler.registration;

import dev.tiltrikt.orion.service.api.model.InstanceModel;
import org.jetbrains.annotations.NotNull;

public interface RegistrationHandler {

    void register(@NotNull InstanceModel model);
}
