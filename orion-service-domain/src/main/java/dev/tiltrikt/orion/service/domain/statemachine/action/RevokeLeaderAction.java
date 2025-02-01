package dev.tiltrikt.orion.service.domain.statemachine.action;

import dev.tiltrikt.orion.service.domain.job.manager.LeaderHeartbeatJobManager;
import dev.tiltrikt.orion.service.domain.job.manager.LeaseExpirationCheckJobManager;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RevokeLeaderAction implements Action<States, Events> {

    @NotNull OrionServiceNode thisOrionServiceNode;

    @NotNull LeaderHeartbeatJobManager leaderHeartbeatJobManager;

    @NotNull LeaseExpirationCheckJobManager leaseExpirationCheckJobManager;

    @Override
    public void execute(StateContext<States, Events> context) {
        log.info("State: LEADER");
        thisOrionServiceNode.setLeader(false);
        leaderHeartbeatJobManager.stopJob();
        leaseExpirationCheckJobManager.stopJob();
    }
}
