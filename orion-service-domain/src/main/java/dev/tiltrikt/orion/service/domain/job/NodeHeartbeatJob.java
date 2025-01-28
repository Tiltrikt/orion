package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.NodeEventPublisher;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
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

    @NotNull OrionServiceNode thisOrionServiceNode;

    @NotNull NodeEventPublisher nodeEventPublisher;

    @Scheduled(
            timeUnit = TimeUnit.SECONDS,
            fixedRate = 5
    )
    public void execute() {
        NodeHeartbeatEvent event = new NodeHeartbeatEvent(
                thisOrionServiceNode.getId(),
                thisOrionServiceNode.getLeaseDuration(),
                String.valueOf(thisOrionServiceNode.isLeader())
        );
        nodeEventPublisher.publishUpdate(event);
    }
}
