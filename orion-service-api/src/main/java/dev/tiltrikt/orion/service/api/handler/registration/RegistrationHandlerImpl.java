package dev.tiltrikt.orion.service.api.handler.registration;

import dev.tiltrikt.orion.service.api.model.InstanceModel;
import dev.tiltrikt.orion.service.api.publisher.EventPublisher;
import dev.tiltrikt.orion.service.api.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationHandlerImpl implements RegistrationHandler {

    @NotNull InstanceService instanceService;

    @NotNull EventPublisher eventPublisher;

    @Override
    public void register(@NotNull InstanceModel model) {
        instanceService.save(model);
        eventPublisher.publishRegistration(model);
    }
}
