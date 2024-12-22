package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.api.event.RegistryUpdateEvent;
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
public class InstanceDeregistrationConsumer {

    @NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate;

    @NotNull LeaseService leaseService;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_DEREGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceDeregistrationEvent event) {
        kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, event.getInstanceId(), null);
        leaseService.deleteById(event.getInstanceId());
    }
}
