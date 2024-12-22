package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.api.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.model.LeaseModel;
import dev.tiltrikt.orion.service.model.LeaseModelFactory;
import dev.tiltrikt.orion.service.service.LeaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceRegistrationConsumer {

    @NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate;

    @NotNull LeaseService leaseService;

    @NotNull LeaseModelFactory leaseModelFactory;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_REGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceRegistrationEvent instanceRegistrationEvent) {
        RegistryUpdateEvent registryUpdateEvent = new RegistryUpdateEvent(
                instanceRegistrationEvent.getInstanceId(),
                instanceRegistrationEvent.getServiceId(),
                instanceRegistrationEvent.getHost(),
                instanceRegistrationEvent.getPort(),
                instanceRegistrationEvent.getMetadata()
        );
        kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC,
                registryUpdateEvent.getInstanceId(),
                registryUpdateEvent
        );
        LeaseModel leaseModel = leaseModelFactory.create(
                instanceRegistrationEvent.getInstanceId(),
                instanceRegistrationEvent.getLeaseDuration()
        );
        leaseService.save(leaseModel);
    }
}
