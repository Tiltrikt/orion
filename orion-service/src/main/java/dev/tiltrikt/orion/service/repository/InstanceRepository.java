package dev.tiltrikt.orion.service.repository;

import dev.tiltrikt.orion.service.model.InstanceModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface InstanceRepository extends JpaRepository<InstanceModel, String> {

    @NotNull List<InstanceModel> findAllByLeaseExpirationTimeBefore(@NotNull Instant now);
}
