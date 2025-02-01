package dev.tiltrikt.orion.service.domain.handler.raft;

import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import dev.tiltrikt.orion.service.common.event.VoteEvent;
import dev.tiltrikt.orion.service.common.publisher.VotePublisher;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.task.manager.ElectionTimeoutTaskManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.region.Region;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateRequestHandlerImpl implements CandidateRequestHandler {

    @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager;

    @NotNull VotePublisher votePublisher;

    @NotNull OrionServiceNode thisOrionServiceNode;

    @NotNull StateMachine<States, Events> stateMachine;

    @Override
    public void handle(@NotNull CandidateRequestEvent event) {
        if (stateMachine.getState().getId() == States.FOLLOWER && event.getTerm() >= thisOrionServiceNode.getTerm()) {
            electionTimeoutTaskManager.refreshTimer();
            votePublisher.publish(new VoteEvent(thisOrionServiceNode.getId(), event.getCandidateId()));
        } else if (event.getTerm() > thisOrionServiceNode.getTerm()) {
            thisOrionServiceNode.setTerm(event.getTerm());
            electionTimeoutTaskManager.refreshTimer();
            votePublisher.publish(new VoteEvent(thisOrionServiceNode.getId(), event.getCandidateId()));
            stateMachine.sendEvent(Events.RECEIVE_APPEND_ENTRIES);
        }
    }
}
