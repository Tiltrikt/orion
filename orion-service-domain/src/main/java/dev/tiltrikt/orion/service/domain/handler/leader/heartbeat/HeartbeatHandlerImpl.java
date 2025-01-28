package dev.tiltrikt.orion.service.domain.handler.leader.heartbeat;

import dev.tiltrikt.orion.common.instance.InstanceState;
import dev.tiltrikt.orion.service.domain.exception.InstanceNotFoundException;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatHandlerImpl implements HeartbeatHandler {

    @NotNull InstanceService instanceService;

    @NotNull InstanceModelFactory instanceModelFactory;

    @Override
    public void update(@NotNull String instanceId) {
        if (!instanceService.existsById(instanceId)) {
            InstanceModel instance = instanceModelFactory.createWithUnknownState(instanceId);
            instanceService.save(instance);
            throw new InstanceNotFoundException("Instance '%s' not exists", instanceId);
        }

        InstanceModel instance = instanceService.getById(instanceId);
        if (instance.getState() == InstanceState.UP) {
            instanceService.renewLicense(instanceId);
        }
    }
}
