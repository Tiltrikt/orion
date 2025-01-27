package dev.tiltrikt.orion.service.domain.handler.follover;

import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import org.jetbrains.annotations.NotNull;

public interface ReplicationHandler {

    void replicate(@NotNull InstanceModel model);
}
