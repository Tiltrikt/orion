package dev.tiltrikt.orion.service.domain.handler;

import dev.tiltrikt.orion.service.domain.model.NodeModel;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NodeHeartbeatHandlerImpl implements NodeHeartbeatHandler {

    @NotNull NodeService nodeService;

    @Override
    @Transactional
    public void update(@NotNull NodeModel nodeModel) {
        nodeService.save(nodeModel);
    }
}
