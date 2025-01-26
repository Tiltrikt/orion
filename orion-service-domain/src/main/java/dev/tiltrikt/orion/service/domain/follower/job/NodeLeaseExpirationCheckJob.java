package dev.tiltrikt.orion.service.domain.follower.job;

import dev.tiltrikt.orion.service.common.manager.ConsumerManager;
import dev.tiltrikt.orion.service.domain.follower.model.Node;
import dev.tiltrikt.orion.service.domain.follower.model.NodeModel;
import dev.tiltrikt.orion.service.domain.follower.service.NodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NodeLeaseExpirationCheckJob {

    @NotNull Node thisNode;

    @NotNull NodeService nodeService;

    @NotNull ConsumerManager consumerManager;

    @Transactional
    @Scheduled(initialDelay = 15000, fixedRate = 5000)
    public void execute() {
        List<NodeModel> nodeModelList = nodeService.getAllExpired();
        nodeService.deleteAll(nodeModelList);
        Optional<NodeModel> leaderNode = nodeService.getLeader();
        if (leaderNode.isEmpty()) {
            NodeModel newLeader = nodeService.chooseLeader();
            newLeader.setLeader(true);
            nodeService.save(newLeader);
            if (newLeader.getId() == thisNode.getId()) {
                thisNode.setLeader(true);
                consumerManager.becomeLeader();
            }
        }
    }
}