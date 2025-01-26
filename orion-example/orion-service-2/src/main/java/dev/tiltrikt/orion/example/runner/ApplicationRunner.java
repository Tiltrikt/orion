package dev.tiltrikt.orion.example.runner;

import dev.tiltrikt.orion.service.common.manager.ConsumerManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationRunner implements CommandLineRunner {

    @NotNull ConsumerManager consumerManager;

    @Override
    public void run(String... args) throws Exception {
        consumerManager.becomeFollower();
    }
}
