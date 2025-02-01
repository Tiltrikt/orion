package dev.tiltrikt.orion.service.domain.handler.raft;

import dev.tiltrikt.orion.service.common.event.VoteEvent;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.service.VoteCounterService;
import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteRequestHandlerImpl implements VoteRequestHandler {

    @NotNull VoteCounterService voteCounterService;

    @NotNull StateMachine<States, Events> stateMachine;

    @NotNull OrionServiceNode thisOrionServiceNode;

    @Override
    public void handle(@NotNull VoteEvent event) {
        if (event.getCandidateId() == thisOrionServiceNode.getId()) {
            voteCounterService.addVote(event.getVoterId());
            if (voteCounterService.getCounter() >= Math.ceil((double) 3 / 2)) {
                stateMachine.sendEvent(Events.WIN_ELECTION);
            }
        }
    }
}