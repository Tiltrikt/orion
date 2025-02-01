package dev.tiltrikt.orion.service.domain.task;

import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.statemachine.StateMachine;

import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElectionTimeoutTask extends TimerTask {

    @NotNull StateMachine<States, Events> stateMachine;

    @Override
    public void run() {
        log.info("Election timeout");
        stateMachine.sendEvent(Events.ELECTION_TIMEOUT);
    }
}
