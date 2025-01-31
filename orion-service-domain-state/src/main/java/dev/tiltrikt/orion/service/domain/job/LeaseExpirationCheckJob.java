package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaseExpirationCheckJob implements Runnable {

    @NotNull RegistryUpdatePublisher registryUpdatePublisher;

    @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher;

    @NotNull InstanceService instanceService;

    @Override
    public void run() {
        List<InstanceModel> instanceModelList = instanceService.getAllExpired();
        instanceService.deleteAll(instanceModelList);
        for (InstanceModel instanceModel : instanceModelList) {
            registryUpdatePublisher.publishDeregistration(instanceModel.getId());
            replicationRegistryUpdatePublisher.publishUpdate(instanceModel.getId(), null);
        }
    }
}
