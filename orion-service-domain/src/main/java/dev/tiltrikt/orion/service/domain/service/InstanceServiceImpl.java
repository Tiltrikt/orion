package dev.tiltrikt.orion.service.domain.service;

import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.exception.InstanceNotFoundException;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.repository.InstanceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceServiceImpl implements InstanceService {

    @NotNull InstanceRepository instanceRepository;

    @NotNull ReplicationRegistryUpdatePublisher registryUpdatePublisher;

    @Override
    @Unmodifiable
    public @NotNull List<InstanceModel> getAllExpired() {
        return List.copyOf(instanceRepository.findAllByLeaseExpirationTimeBefore(Instant.now()));
    }

    @Override
    public boolean existsById(@NotNull String instanceId) {
        return instanceRepository.existsById(instanceId);
    }

    @Override
    public @NotNull InstanceModel getById(@NotNull String instanceId) {
        return instanceRepository.findById(instanceId)
                .orElseThrow(() -> new InstanceNotFoundException("Instance '%s' not exists", instanceId));
    }

    @Override
    public @NotNull InstanceModel save(@NotNull InstanceModel instanceModel) {
        ReplicationEvent event = new ReplicationEvent(
                instanceModel.getId(),
                instanceModel.getServiceId(),
                instanceModel.getHost(),
                instanceModel.getPort(),
                instanceModel.getLeaseDuration(),
                instanceModel.getMetadata(),
                instanceModel.getLeaseExpirationTime(),
                instanceModel.getState()
        );
        registryUpdatePublisher.publishUpdate(instanceModel.getId(), event);
        return instanceRepository.save(instanceModel);
    }

    @Override
    public void deleteAll(@NotNull List<InstanceModel> modelList) {
        instanceRepository.deleteAllInBatch(modelList);
    }

    @Override
    public void deleteById(@NotNull String instanceId) {
        registryUpdatePublisher.publishUpdate(instanceId, null);
        instanceRepository.deleteById(instanceId);
    }

    @Override
    public @NotNull List<InstanceModel> findAll() {
        return List.copyOf(instanceRepository.findAll());
    }

    @Override
    public @NotNull InstanceModel renewLicense(@NotNull String instanceId) {
        InstanceModel instanceModel = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new InstanceNotFoundException("Instance '%s' not exists", instanceId));
        instanceModel.setLeaseExpirationTime(Instant.now().plusSeconds(instanceModel.getLeaseDuration()));
        return save(instanceModel);
    }
}
