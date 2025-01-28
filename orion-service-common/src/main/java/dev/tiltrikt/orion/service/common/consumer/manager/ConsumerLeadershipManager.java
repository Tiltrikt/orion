package dev.tiltrikt.orion.service.common.consumer.manager;

import dev.tiltrikt.orion.service.common.event.BecomeFollowerEvent;
import dev.tiltrikt.orion.service.common.event.BecomeLeaderEvent;
import org.jetbrains.annotations.NotNull;

public interface ConsumerLeadershipManager {

    void becomeLeader(@NotNull BecomeLeaderEvent event);

    void becomeFollower(@NotNull BecomeFollowerEvent event);
}
