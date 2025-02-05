package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerLeadershipManager;
import dev.tiltrikt.orion.service.common.publisher.CandidateRequestPublisher;
import dev.tiltrikt.orion.service.domain.handler.raft.VoteRequestHandler;
import dev.tiltrikt.orion.service.domain.job.manager.LeaderHeartbeatJobManager;
import dev.tiltrikt.orion.service.domain.job.manager.LeaseExpirationCheckJobManager;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.service.VoteCounterService;
import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.statemachine.action.*;
import dev.tiltrikt.orion.service.domain.task.manager.ElectionTimeoutTaskManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Slf4j
@Configuration
@EnableStateMachine
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RaftStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Lazy
    @NotNull BecomeCandidateAction becomeCandidateAction;
    @Lazy
    @NotNull BecomeLeaderAction becomeLeaderAction;
    @Lazy
    @NotNull BecomeFollowerAction becomeFollowerAction;

    @Lazy
    @NotNull RevokeCandidateAction revokeCandidateAction;
    @Lazy
    @NotNull RevokeLeaderAction revokeLeaderAction;
    @Lazy
    @NotNull RevokeFollowerAction revokeFollowerAction;

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.FOLLOWER, becomeFollowerAction)
                .state(States.CANDIDATE)
                .state(States.LEADER);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.FOLLOWER)
                .target(States.CANDIDATE)
                .event(Events.ELECTION_TIMEOUT)
                .action(revokeFollowerAction)
                .action(becomeCandidateAction)

                .and()
                .withExternal()
                .source(States.CANDIDATE)
                .target(States.CANDIDATE)
                .event(Events.ELECTION_TIMEOUT)
                .action(revokeCandidateAction)
                .action(becomeCandidateAction)

                .and()
                .withExternal()
                .source(States.CANDIDATE)
                .target(States.LEADER)
                .event(Events.WIN_ELECTION)
                .action(revokeCandidateAction)
                .action(becomeLeaderAction)

                .and()
                .withExternal()
                .source(States.CANDIDATE)
                .target(States.FOLLOWER)
                .event(Events.RECEIVE_APPEND_ENTRIES)
                .action(revokeCandidateAction)
                .action(becomeFollowerAction)

                .and()
                .withExternal()
                .source(States.LEADER)
                .target(States.FOLLOWER)
                .event(Events.STEP_DOWN)
                .action(revokeLeaderAction)
                .action(becomeFollowerAction);
    }

    @Bean
    @NotNull BecomeCandidateAction becomeCandidateAction(
            @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager,
            @NotNull CandidateRequestPublisher candidateRequestPublisher,
            @NotNull VoteCounterService voteCounterService,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull VoteRequestHandler voteRequestHandler
            ) {
        return new BecomeCandidateAction(
                electionTimeoutTaskManager,
                candidateRequestPublisher,
                voteCounterService,
                thisOrionServiceNode,
                voteRequestHandler
        );
    }

    @Bean
    @NotNull BecomeFollowerAction becomeFollowerAction(
            @NotNull ConsumerLeadershipManager consumerLeadershipManager,
            @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager
    ) {
        return new BecomeFollowerAction(consumerLeadershipManager, electionTimeoutTaskManager);
    }

    @Bean
    @NotNull BecomeLeaderAction becomeLeaderAction(
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull ConsumerLeadershipManager consumerLeadershipManager,
            @NotNull LeaderHeartbeatJobManager leaderHeartbeatJobManager,
            @NotNull LeaseExpirationCheckJobManager leaseExpirationCheckJobManager
    ) {
        return new BecomeLeaderAction(
                thisOrionServiceNode,
                consumerLeadershipManager,
                leaderHeartbeatJobManager,
                leaseExpirationCheckJobManager
        );
    }

    @Bean
    @NotNull RevokeCandidateAction revokeCandidateAction(@NotNull VoteCounterService voteCounterService) {
        return new RevokeCandidateAction(voteCounterService);
    }

    @Bean
    @NotNull RevokeLeaderAction revokeLeaderAction(
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull LeaderHeartbeatJobManager leaderHeartbeatJobManager,
            @NotNull LeaseExpirationCheckJobManager leaseExpirationCheckJobManager
    ) {
        return new RevokeLeaderAction(
                thisOrionServiceNode,
                leaderHeartbeatJobManager,
                leaseExpirationCheckJobManager
        );
    }

    @Bean
    @NotNull RevokeFollowerAction revokeFollowerAction() {
        return new RevokeFollowerAction();
    }
}