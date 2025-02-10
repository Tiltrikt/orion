package dev.tiltrikt.orion.service.domain.handler.leader.heartbeat;

import dev.tiltrikt.orion.common.instance.InstanceState;
import dev.tiltrikt.orion.service.common.publisher.HeartbeatErrorPublisher;
import dev.tiltrikt.orion.service.domain.exception.InstanceNotFoundException;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatHandlerImpl implements HeartbeatHandler {

    @NotNull InstanceService instanceService;

    @NotNull InstanceModelFactory instanceModelFactory;

    @NotNull HeartbeatErrorPublisher heartbeatErrorPublisher;

    @Override
    public void update(@NotNull String instanceId) {
        if (!instanceService.existsById(instanceId)) {
            InstanceModel instance = instanceModelFactory.createWithUnknownState(instanceId);
            instanceService.save(instance);
            heartbeatErrorPublisher.publishError(instanceId);
        }

        InstanceModel instance = instanceService.getById(instanceId);
        if (instance.getState() == InstanceState.UP) {
            instanceService.renewLicense(instanceId);
        }
    }

    @Override
    public void updateBatch(@NotNull List<String> instanceIdList) {
        List<InstanceModel> existingInstances = instanceService.getAllById(instanceIdList);
        List<String> upInstanceIdList = existingInstances.stream()
                .filter(instance -> instance.getState() == InstanceState.UP)
                .map(InstanceModel::getId)
                .toList();
        for (String id : upInstanceIdList) {
            instanceService.renewLicense(id);
        }

        List<String> missingInstances = instanceIdList.stream()
                .filter(id -> !instanceService.existsById(id))
                .distinct()
                .toList();

        if (!missingInstances.isEmpty()) {
            List<InstanceModel> newInstances = missingInstances.stream()
                    .map(instanceModelFactory::createWithUnknownState)
                    .toList();
            instanceService.saveAll(newInstances);
            System.out.println("Before exception thrown: "+ missingInstances);
            for (String missingInstance : missingInstances) {
                heartbeatErrorPublisher.publishError(missingInstance);
            }
        }
    }
}
