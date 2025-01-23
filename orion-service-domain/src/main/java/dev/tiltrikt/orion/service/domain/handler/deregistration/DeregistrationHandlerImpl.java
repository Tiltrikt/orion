package dev.tiltrikt.orion.service.domain.handler.deregistration;

import dev.tiltrikt.orion.service.common.publisher.EventPublisher;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeregistrationHandlerImpl implements DeregistrationHandler {

    @NotNull InstanceService instanceService;

    @NotNull EventPublisher eventPublisher;


    @Override
    public void deregister(@NotNull String instanceId) {
        instanceService.deleteById(instanceId);
        eventPublisher.publishDeregistration(instanceId);
    }
}
