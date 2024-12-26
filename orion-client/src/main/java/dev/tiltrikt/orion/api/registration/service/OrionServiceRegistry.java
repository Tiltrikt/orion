package dev.tiltrikt.orion.api.registration.service;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.configuration.OrionClientConfigurationProperties;
import dev.tiltrikt.orion.api.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.api.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.api.model.OrionInstance;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionServiceRegistry implements ServiceRegistry<OrionInstance> {

    @NotNull KafkaTemplate<String, InstanceRegistrationEvent> registrationKafkaTemplate;

    @NotNull KafkaTemplate<String, InstanceDeregistrationEvent> deregistrationKafkaTemplate;

    @NotNull OrionClientConfigurationProperties orionClientConfigurationProperties;

    @Override
    public void register(@NotNull OrionInstance registration) {
        InstanceRegistrationEvent instanceRegistrationEvent = new InstanceRegistrationEvent(
                registration.getServiceId(),
                registration.getHost(),
                registration.getPort(),
                orionClientConfigurationProperties.getLeaseDurationSec(),
                registration.getMetadata()
        );
        registrationKafkaTemplate.send(KafkaTopicConfiguration.INSTANCE_REGISTRATION_TOPIC, instanceRegistrationEvent);
    }

    @Override
    public void deregister(@NotNull OrionInstance registration) {
        InstanceDeregistrationEvent instanceDeregistrationEvent = new InstanceDeregistrationEvent(
                registration.getInstanceId()
        );
        deregistrationKafkaTemplate.send(
                KafkaTopicConfiguration.INSTANCE_DEREGISTRATION_TOPIC,
                instanceDeregistrationEvent
        );
    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(OrionInstance registration, String status) {
    }

    @Override
    public <T> T getStatus(OrionInstance registration) {
        return null;
    }
}
