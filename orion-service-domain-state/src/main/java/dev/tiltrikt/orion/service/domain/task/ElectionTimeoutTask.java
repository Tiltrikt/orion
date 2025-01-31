package dev.tiltrikt.orion.service.domain.task;

import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.statemachine.StateMachine;

import java.util.TimerTask;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElectionTimeoutTask extends TimerTask {

    @NotNull StateMachine<States, Events> stateMachine;

    @Override
    public void run() {
        stateMachine.sendEvent(Events.ELECTION_TIMEOUT);
    }
}
