package dev.tiltrikt.orion.service.domain.leadership;

import dev.tiltrikt.orion.service.common.event.BecomeLeaderEvent;
import dev.tiltrikt.orion.service.domain.model.NodeModel;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeadershipManagerImpl implements LeadershipManager {

    @NotNull OrionServiceNode thisOrionServiceNode;

    @NotNull ApplicationEventPublisher applicationEventPublisher;

    @NotNull NodeService nodeService;

    public void chooseNewLeader() {
        NodeModel newLeader = nodeService.chooseLeader();
        newLeader.setLeader(true);
        nodeService.save(newLeader);
        if (isThisNode(newLeader)) {
            applicationEventPublisher.publishEvent(new BecomeLeaderEvent(this));
        }
    }

    private boolean isThisNode(@NotNull NodeModel node) {
        return node.getId() == thisOrionServiceNode.getId();
    }
}
