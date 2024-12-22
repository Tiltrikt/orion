package dev.tiltrikt.orion.service.service;

import dev.tiltrikt.orion.service.exception.LeaseException;
import dev.tiltrikt.orion.service.model.LeaseModel;
import dev.tiltrikt.orion.service.repository.LeaseRepository;
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
public class LeaseServiceImpl implements LeaseService {

    @NotNull LeaseRepository leaseRepository;

    @Override
    @Unmodifiable
    public @NotNull List<LeaseModel> getAllExpired() {
        return List.copyOf(leaseRepository.findAllByLeaseExpirationTimeBefore(Instant.now()));
    }

    @Override
    public @NotNull LeaseModel save(@NotNull LeaseModel leaseModel) {
        return leaseRepository.save(leaseModel);
    }

    @Override
    public void deleteAll(@NotNull List<LeaseModel> modelList) {
        leaseRepository.deleteAllInBatch(modelList);
    }

    @Override
    public void deleteById(@NotNull String instanceId) {
        leaseRepository.deleteById(instanceId);
    }

    @Override
    public @NotNull LeaseModel renewLicense(@NotNull String instanceId) {
        LeaseModel leaseModel = leaseRepository.findById(instanceId)
                .orElseThrow(() -> new LeaseException("Instance '%s' not exists or have been already removed due to licence expiration", instanceId));
        leaseModel.setLeaseExpirationTime(Instant.now().plusSeconds(leaseModel.getLeaseDuration()));
        return leaseRepository.save(leaseModel);
    }
}
