package dev.tiltrikt.orion.service.api.service;

import dev.tiltrikt.orion.service.api.exception.InstanceException;
import dev.tiltrikt.orion.service.api.model.InstanceModel;
import dev.tiltrikt.orion.service.api.repository.InstanceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceServiceImpl implements InstanceService {

    @NotNull InstanceRepository instanceRepository;

    @Override
    @Unmodifiable
    public @NotNull List<InstanceModel> getAllExpired() {
        return List.copyOf(instanceRepository.findAllByLeaseExpirationTimeBefore(Instant.now()));
    }

    @Override
    public @NotNull InstanceModel getById(@NotNull String instanceId) {
        return instanceRepository.findById(instanceId)
                .orElseThrow(() -> new InstanceException("Instance '%s' not exists", instanceId));
    }

    @Override
    public @NotNull InstanceModel save(@NotNull InstanceModel instanceModel) {
        return instanceRepository.save(instanceModel);
    }

    @Override
    public void deleteAll(@NotNull List<InstanceModel> modelList) {
        instanceRepository.deleteAllInBatch(modelList);
    }

    @Override
    public void deleteById(@NotNull String instanceId) {
        instanceRepository.deleteById(instanceId);
    }

    @Override
    public @NotNull InstanceModel renewLicense(@NotNull String instanceId) {
        InstanceModel instanceModel = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new InstanceException("Instance '%s' not exists", instanceId));
        instanceModel.setLeaseExpirationTime(Instant.now().plusSeconds(instanceModel.getLeaseDuration()));
        return instanceRepository.save(instanceModel);
    }
}
