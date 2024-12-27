package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.api.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.model.InstanceModel;
import dev.tiltrikt.orion.service.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceRegistrationConsumer {

    @NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate;

    @NotNull InstanceService instanceService;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_REGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceRegistrationEvent instanceRegistrationEvent) {
        RegistryUpdateEvent registryUpdateEvent = new RegistryUpdateEvent(
                instanceRegistrationEvent.getServiceId(),
                instanceRegistrationEvent.getHost(),
                instanceRegistrationEvent.getPort(),
                instanceRegistrationEvent.getMetadata()
        );
        kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC,
                registryUpdateEvent.getInstanceId(),
                registryUpdateEvent
        );
        InstanceModel instanceModel = new InstanceModel(
                instanceRegistrationEvent.getInstanceId(),
                instanceRegistrationEvent.getServiceId(),
                instanceRegistrationEvent.getHost(),
                instanceRegistrationEvent.getPort(),
                instanceRegistrationEvent.getMetadata(),
                instanceRegistrationEvent.getLeaseDuration(),
                Instant.now().plusSeconds(instanceRegistrationEvent.getLeaseDuration())
        );
        instanceService.save(instanceModel);
    }
}
