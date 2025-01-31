package dev.tiltrikt.orion.service.domain.statemachine.action;

import dev.tiltrikt.orion.service.domain.service.VoteCounterService;
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
public class RevokeCandidateAction implements Action<States, Events> {

    @NotNull VoteCounterService voteCounterService;

    @Override
    public void execute(StateContext<States, Events> context) {
        log.info("Revoke candidate");
        voteCounterService.resetCounter();
    }
}
