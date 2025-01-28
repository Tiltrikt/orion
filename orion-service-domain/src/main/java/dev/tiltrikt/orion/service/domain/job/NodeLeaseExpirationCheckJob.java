package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.service.domain.leadership.LeadershipManager;
import dev.tiltrikt.orion.service.domain.model.NodeModel;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NodeLeaseExpirationCheckJob {

    @NotNull NodeService nodeService;

    @NotNull LeadershipManager leadershipManager;

    @Scheduled(initialDelay = 15000, fixedRate = 5000)
    public void execute() {
        List<NodeModel> nodeModelList = nodeService.getAllExpired();
        nodeService.deleteAll(nodeModelList);
        if (!nodeService.existsLeader()) {
            leadershipManager.chooseNewLeader();
        }
    }
}