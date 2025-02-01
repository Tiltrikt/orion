package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.task.manager.ElectionTimeoutTaskManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.statemachine.StateMachine;

@Configuration
public class TaskManagerConfiguration {

    @Bean
    @NotNull ElectionTimeoutTaskManager electionTimeoutTaskManager(@Lazy @NotNull StateMachine<States, Events> stateMachine) {
        return new ElectionTimeoutTaskManager(stateMachine);
    }
}
