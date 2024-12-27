package dev.tiltrikt.orion.service.api.handler.heartbeat;

import dev.tiltrikt.orion.service.api.model.InstanceModel;
import dev.tiltrikt.orion.service.api.publisher.EventPublisher;
import dev.tiltrikt.orion.service.api.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatHandlerImpl implements HeartbeatHandler {

    @NotNull InstanceService instanceService;

    @NotNull EventPublisher eventPublisher;

    @Override
    public void update(@NotNull String instanceId) {
        InstanceModel instance = instanceService.getById(instanceId);
        if (instance.getLeaseExpirationTime().isBefore(Instant.now())) {
            eventPublisher.publishRegistration(instance);
        }
        instanceService.renewLicense(instanceId);
    }
}
