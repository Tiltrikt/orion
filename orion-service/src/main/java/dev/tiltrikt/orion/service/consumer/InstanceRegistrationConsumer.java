package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceRegistrationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceRegistrationConsumer {

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_REGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceRegistrationEvent event) {
        System.out.println(event);
    }
}
