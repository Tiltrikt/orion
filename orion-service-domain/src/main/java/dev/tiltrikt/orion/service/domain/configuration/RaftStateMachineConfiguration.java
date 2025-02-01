package dev.tiltrikt.orion.service.domain.configuration;

import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.statemachine.action.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
@EnableStateMachine
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RaftStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {

    @NotNull BecomeCandidateAction becomeCandidateAction;
    @NotNull BecomeLeaderAction becomeLeaderAction;
    @NotNull BecomeFollowerAction becomeFollowerAction;

    @NotNull RevokeCandidateAction revokeCandidateAction;
    @NotNull RevokeLeaderAction revokeLeaderAction;
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
}