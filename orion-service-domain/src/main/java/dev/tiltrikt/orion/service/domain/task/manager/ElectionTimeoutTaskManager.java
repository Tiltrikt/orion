package dev.tiltrikt.orion.service.domain.task.manager;

import dev.tiltrikt.orion.service.domain.statemachine.Events;
import dev.tiltrikt.orion.service.domain.statemachine.States;
import dev.tiltrikt.orion.service.domain.task.ElectionTimeoutTask;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.statemachine.StateMachine;

import java.util.Random;
import java.util.Timer;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElectionTimeoutTaskManager {

    @NotNull Random random = new Random();

    @Lazy
    @NotNull StateMachine<States, Events> stateMachine;

    @NonFinal
    @Nullable Timer timer;

    public void refreshTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        ElectionTimeoutTask electionTimeoutTask = new ElectionTimeoutTask(stateMachine);
        int leaseExpiration = 5000 + random.nextInt(200) * 10;
        timer.schedule(electionTimeoutTask, leaseExpiration);
    }
}