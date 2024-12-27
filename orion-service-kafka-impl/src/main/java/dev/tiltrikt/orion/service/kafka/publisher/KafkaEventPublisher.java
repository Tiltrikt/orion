package dev.tiltrikt.orion.service.kafka.publisher;

import dev.tiltrikt.orion.service.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.api.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.api.model.InstanceModel;
import dev.tiltrikt.orion.service.api.publisher.EventPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaEventPublisher implements EventPublisher {

    @NotNull KafkaTemplate<String, RegistryUpdateEvent> registryUpdateEventKafkaTemplate;

    @Override
    public void publishRegistration(@NotNull InstanceModel instance) {
        RegistryUpdateEvent event = new RegistryUpdateEvent(
                instance.getServiceId(),
                instance.getHost(),
                instance.getPort(),
                instance.getMetadata()
        );
        registryUpdateEventKafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instance.getId(), event);
    }

    @Override
    public void publishDeregistration(@NotNull String instanceId) {
        registryUpdateEventKafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instanceId, null);
    }
}
