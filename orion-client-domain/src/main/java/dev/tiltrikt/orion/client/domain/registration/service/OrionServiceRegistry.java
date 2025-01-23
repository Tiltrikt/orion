package dev.tiltrikt.orion.client.domain.registration.service;

import dev.tiltrikt.orion.client.common.publisher.EventPublisher;
import dev.tiltrikt.orion.client.domain.model.OrionInstance;
import dev.tiltrikt.orion.common.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.common.event.InstanceRegistrationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionServiceRegistry implements ServiceRegistry<OrionInstance> {

    @NotNull EventPublisher eventPublisher;

    int leaseDurationSec;

    @Override
    public void register(@NotNull OrionInstance registration) {
        InstanceRegistrationEvent instanceRegistrationEvent = new InstanceRegistrationEvent(
                registration.getServiceId(),
                registration.getHost(),
                registration.getPort(),
                leaseDurationSec,
                registration.getMetadata()
        );
        eventPublisher.publishRegistration(instanceRegistrationEvent);
    }

    @Override
    public void deregister(@NotNull OrionInstance registration) {
        InstanceDeregistrationEvent instanceDeregistrationEvent = new InstanceDeregistrationEvent(
                registration.getInstanceId()
        );
        eventPublisher.publishDeregistration(instanceDeregistrationEvent);
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
