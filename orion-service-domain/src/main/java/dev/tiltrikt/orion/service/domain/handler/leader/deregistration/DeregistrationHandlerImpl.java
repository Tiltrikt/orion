package dev.tiltrikt.orion.service.domain.handler.leader.deregistration;

import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeregistrationHandlerImpl implements DeregistrationHandler {

    @NotNull InstanceService instanceService;

    @NotNull RegistryUpdatePublisher registryUpdatePublisher;


    @Override
    public void deregister(@NotNull String instanceId) {
        instanceService.deleteById(instanceId);
        registryUpdatePublisher.publishDeregistration(instanceId);
    }

    @Override
    public void deregisterBatch(@NotNull List<String> instanceIdList) {
        instanceService.deleteAllById(instanceIdList);
        instanceIdList.forEach(registryUpdatePublisher::publishDeregistration);
    }
}
