package dev.tiltrikt.orion.client.kafka.publisher;

import dev.tiltrikt.orion.client.common.publisher.EventPublisher;
import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.common.event.InstanceRegistrationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaEventPublisher implements EventPublisher {

    @NotNull KafkaTemplate<String, InstanceHeartbeatEvent> instanceHeartbeatEventKafkaTemplate;

    @NotNull KafkaTemplate<String, InstanceDeregistrationEvent> instanceDeregistrationEventKafkaTemplate;

    @NotNull KafkaTemplate<String, InstanceRegistrationEvent> instanceRegistrationEventKafkaTemplate;

    @Override
    public void publishRegistration(@NotNull InstanceRegistrationEvent event) {
        instanceRegistrationEventKafkaTemplate.send(KafkaTopicConfiguration.INSTANCE_REGISTRY_TOPIC, event);
    }

    @Override
    public void publishDeregistration(@NotNull InstanceDeregistrationEvent event) {
        instanceDeregistrationEventKafkaTemplate.send(KafkaTopicConfiguration.INSTANCE_REGISTRY_TOPIC, event);
    }

    @Override
    public void publishHeartbeat(@NotNull InstanceHeartbeatEvent event) {
        instanceHeartbeatEventKafkaTemplate.send(KafkaTopicConfiguration.INSTANCE_REGISTRY_TOPIC, event);
    }

}
