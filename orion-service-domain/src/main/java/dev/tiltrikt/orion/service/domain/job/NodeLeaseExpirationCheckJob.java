package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.service.common.leadership.LeadershipManager;
import dev.tiltrikt.orion.service.domain.model.Node;
import dev.tiltrikt.orion.service.domain.model.NodeModel;
import dev.tiltrikt.orion.service.domain.service.NodeService;
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

    @NotNull LeadershipManager leadershipManager;

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
                leadershipManager.becomeLeader();
            }
        }
    }
}