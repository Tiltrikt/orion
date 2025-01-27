package dev.tiltrikt.orion.service.domain.handler;

import dev.tiltrikt.orion.service.domain.model.NodeModel;
import org.jetbrains.annotations.NotNull;

public interface NodeHeartbeatHandler {

    void update(@NotNull NodeModel nodeModel);
}
