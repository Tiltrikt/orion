package dev.tiltrikt.orion.service.domain.statemachine.action;

import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import dev.tiltrikt.orion.service.common.event.VoteEvent;
import dev.tiltrikt.orion.service.common.publisher.CandidateRequestPublisher;
import dev.tiltrikt.orion.service.domain.handler.raft.VoteRequestHandler;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.service.VoteCounterService;
import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.task.manager.ElectionTimeoutTaskManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BecomeCandidateAction implements Action<States, Events> {

    @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager;

    @NotNull CandidateRequestPublisher candidateRequestPublisher;

    @NotNull VoteCounterService voteCounterService;

    @NotNull OrionServiceNode thisOrionServiceNode;

    @NotNull VoteRequestHandler voteRequestHandler;

    @Override
    public void execute(StateContext<States, Events> context) {
        log.info("   |");
        log.info("   V");
        log.info("State: CANDIDATE");
        voteRequestHandler.handle(new VoteEvent(thisOrionServiceNode.getId(), thisOrionServiceNode.getId()));
        voteCounterService.resetCounter();
        voteCounterService.addVote(thisOrionServiceNode.getId());
        candidateRequestPublisher.publish(new CandidateRequestEvent(thisOrionServiceNode.getId(), thisOrionServiceNode.getTerm()));
        electionTimeoutTaskManager.refreshTimer();
    }
}
