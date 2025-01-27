package dev.tiltrikt.orion.service.domain.handler.leader.heartbeat;

import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatHandlerImpl implements HeartbeatHandler {

    @NotNull InstanceService instanceService;

    @NotNull RegistryUpdatePublisher registryUpdatePublisher;

    @Override
    public void update(@NotNull String instanceId) {
        InstanceModel instance = instanceService.getById(instanceId);
        if (instance.getLeaseExpirationTime().isBefore(Instant.now())) {
            RegistryUpdateEvent event = new RegistryUpdateEvent(
                    instance.getServiceId(),
                    instance.getHost(),
                    instance.getPort(),
                    instance.getMetadata()
            );
            registryUpdatePublisher.publishRegistration(instanceId, event);
        }
        instanceService.renewLicense(instanceId);
    }
}
