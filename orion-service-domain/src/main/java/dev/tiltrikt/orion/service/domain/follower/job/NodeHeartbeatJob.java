package dev.tiltrikt.orion.service.domain.follower.job;

import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.NodeEventPublisher;
import dev.tiltrikt.orion.service.domain.follower.model.Node;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NodeHeartbeatJob {

    @NotNull Node thisNode;

    @NotNull NodeEventPublisher nodeEventPublisher;

    @Scheduled(
            timeUnit = TimeUnit.SECONDS,
            fixedRate = 5
    )
    public void execute() {
        NodeHeartbeatEvent event = new NodeHeartbeatEvent(
                thisNode.getId(),
                thisNode.getLeaseDuration(),
                String.valueOf(thisNode.isLeader())
        );
        nodeEventPublisher.publishUpdate(event);
    }
}
