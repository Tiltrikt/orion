package dev.tiltrikt.orion.service.kafka.autoconfiguration;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.event.BecomeFollowerEvent;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplicationAutoconfiguration {

    @NotNull ApplicationEventPublisher applicationEventPublisher;

    @Qualifier("kafkaNodeEventConsumerManager")
    @NotNull ConsumerManager nodeEventConsumerManager;

    @PostConstruct
    public void init() {
        nodeEventConsumerManager.startListening();
        applicationEventPublisher.publishEvent(new BecomeFollowerEvent(this));
    }
}
