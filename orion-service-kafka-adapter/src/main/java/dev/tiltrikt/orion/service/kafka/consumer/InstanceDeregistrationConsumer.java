package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.service.domain.handler.deregistration.DeregistrationHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceDeregistrationConsumer {

    @NotNull DeregistrationHandler deregistrationHandler;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_DEREGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceDeregistrationEvent event) {
        deregistrationHandler.deregister(event.getInstanceId());
    }
}
