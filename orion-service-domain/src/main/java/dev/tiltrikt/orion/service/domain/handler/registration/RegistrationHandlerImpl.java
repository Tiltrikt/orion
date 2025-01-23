package dev.tiltrikt.orion.service.domain.handler.registration;

import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.publisher.EventPublisher;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationHandlerImpl implements RegistrationHandler {

    @NotNull InstanceService instanceService;

    @NotNull EventPublisher eventPublisher;

    @Override
    public void register(@NotNull InstanceModel model) {
        instanceService.save(model);
        RegistryUpdateEvent event = new RegistryUpdateEvent(
                model.getServiceId(),
                model.getHost(),
                model.getPort(),
                model.getMetadata()
        );
        eventPublisher.publishRegistration(model.getId(), event);
    }
}
