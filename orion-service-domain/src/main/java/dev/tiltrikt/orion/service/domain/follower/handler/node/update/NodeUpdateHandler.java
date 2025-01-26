package dev.tiltrikt.orion.service.domain.follower.handler.node.update;

import dev.tiltrikt.orion.service.domain.follower.model.NodeModel;
import org.jetbrains.annotations.NotNull;

public interface NodeUpdateHandler {

    void update(@NotNull NodeModel nodeModel);
}
