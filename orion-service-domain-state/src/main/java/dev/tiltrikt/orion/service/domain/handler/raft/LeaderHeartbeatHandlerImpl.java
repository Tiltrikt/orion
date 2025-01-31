package dev.tiltrikt.orion.service.domain.handler.raft;

import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.task.manager.ElectionTimeoutTaskManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaderHeartbeatHandlerImpl implements LeaderHeartbeatHandler {

    @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager;

    @NotNull StateMachine<States, Events> stateMachine;

    @Override
    public void handle(@NotNull LeaderHeartbeatEvent event) {
        electionTimeoutTaskManager.refreshTimer();
        stateMachine.sendEvent(Events.RECEIVE_APPEND_ENTRIES);
    }
}