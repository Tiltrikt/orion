package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.service.domain.handler.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceRegistrationConsumer {

    @NotNull RegistrationHandler registrationHandler;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_REGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceRegistrationEvent instanceRegistrationEvent) {
        InstanceModel instanceModel = new InstanceModel(
                instanceRegistrationEvent.getInstanceId(),
                instanceRegistrationEvent.getServiceId(),
                instanceRegistrationEvent.getHost(),
                instanceRegistrationEvent.getPort(),
                instanceRegistrationEvent.getMetadata(),
                instanceRegistrationEvent.getLeaseDuration(),
                Instant.now().plusSeconds(instanceRegistrationEvent.getLeaseDuration())
        );
        registrationHandler.register(instanceModel);
    }
}
