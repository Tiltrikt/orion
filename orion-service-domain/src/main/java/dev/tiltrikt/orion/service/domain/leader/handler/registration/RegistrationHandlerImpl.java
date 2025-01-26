package dev.tiltrikt.orion.service.domain.leader.handler.registration;

import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
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

    @NotNull RegistryUpdatePublisher registryUpdatePublisher;

    @Override
    public void register(@NotNull InstanceModel model) {
        instanceService.save(model);
        RegistryUpdateEvent event = new RegistryUpdateEvent(
                model.getServiceId(),
                model.getHost(),
                model.getPort(),
                model.getMetadata()
        );
        registryUpdatePublisher.publishRegistration(model.getId(), event);
    }
}
