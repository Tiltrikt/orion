package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.api.model.Instance;
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

    @NotNull KafkaTemplate<String, Instance> kafkaTemplate;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_DEREGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceDeregistrationEvent event) {
        kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, event.getInstanceId(), null);
    }
}
