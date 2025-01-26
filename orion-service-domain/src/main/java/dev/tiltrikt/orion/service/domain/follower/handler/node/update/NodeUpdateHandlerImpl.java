package dev.tiltrikt.orion.service.domain.follower.handler.node.update;

import dev.tiltrikt.orion.service.domain.follower.model.NodeModel;
import dev.tiltrikt.orion.service.domain.follower.service.NodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NodeUpdateHandlerImpl implements NodeUpdateHandler {

    @NotNull NodeService nodeService;

    @Override
    @Transactional
    public void update(@NotNull NodeModel nodeModel) {
        nodeService.save(nodeModel);
    }
}
