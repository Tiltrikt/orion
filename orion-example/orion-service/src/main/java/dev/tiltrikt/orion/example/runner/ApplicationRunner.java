package dev.tiltrikt.orion.example.runner;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.leadership.LeadershipManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationRunner implements CommandLineRunner {

    @NotNull LeadershipManager leadershipManager;

    @Qualifier("kafkaNodeEventConsumerManager")
    @NotNull ConsumerManager nodeEventConsumerManager;

    @Override
    public void run(String... args) throws Exception {
        nodeEventConsumerManager.startListening();
        leadershipManager.becomeFollower();
    }
}
