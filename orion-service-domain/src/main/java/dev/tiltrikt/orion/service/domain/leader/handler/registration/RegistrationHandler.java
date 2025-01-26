package dev.tiltrikt.orion.service.domain.leader.handler.registration;

import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import org.jetbrains.annotations.NotNull;

public interface RegistrationHandler {

    void register(@NotNull InstanceModel model);
}
