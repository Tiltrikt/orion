package dev.tiltrikt.orion.service.kafka.autoconfiguration;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplicationAutoconfiguration implements CommandLineRunner {

    @Qualifier("kafkaNodeEventConsumerManager")
    @NotNull ConsumerManager nodeEventConsumerManager;

    @Override
    public void run(String... args) throws Exception {
        nodeEventConsumerManager.startListening();
    }
}
