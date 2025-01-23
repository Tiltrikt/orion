package dev.tiltrikt.orion.service.kafka.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.publisher.EventPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaEventPublisher implements EventPublisher {

    @NotNull KafkaTemplate<String, RegistryUpdateEvent> registryUpdateEventKafkaTemplate;

    @Override
    public void publishRegistration(@NotNull String instanceId, @NotNull RegistryUpdateEvent event) {
        registryUpdateEventKafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instanceId, event);
    }

    @Override
    public void publishDeregistration(@NotNull String instanceId) {
        registryUpdateEventKafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instanceId, null);
    }
}
