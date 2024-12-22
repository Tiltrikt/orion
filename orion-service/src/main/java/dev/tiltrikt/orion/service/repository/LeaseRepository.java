package dev.tiltrikt.orion.service.repository;

import dev.tiltrikt.orion.service.model.LeaseModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface LeaseRepository extends JpaRepository<LeaseModel, String> {

    @NotNull List<LeaseModel> findAllByLeaseExpirationTimeBefore(@NotNull Instant now);
}
