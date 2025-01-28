package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaseExpirationCheckJob {

    @NotNull RegistryUpdatePublisher registryUpdatePublisher;

    @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher;

    @NotNull InstanceService instanceService;

    @Scheduled(fixedRate = 5000)
    public void execute() {
        List<InstanceModel> instanceModelList = instanceService.getAllExpired();
        instanceService.deleteAll(instanceModelList);
        for (InstanceModel instanceModel : instanceModelList) {
            registryUpdatePublisher.publishDeregistration(instanceModel.getId());
            replicationRegistryUpdatePublisher.publishUpdate(instanceModel.getId(), null);
        }
    }
}
