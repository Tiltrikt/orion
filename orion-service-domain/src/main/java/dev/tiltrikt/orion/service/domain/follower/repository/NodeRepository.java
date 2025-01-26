package dev.tiltrikt.orion.service.domain.follower.repository;

import dev.tiltrikt.orion.service.domain.follower.model.NodeModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface NodeRepository extends JpaRepository<NodeModel, Integer> {

    @NotNull List<NodeModel> findAllByLeaseExpirationTimeBefore(@NotNull Instant now);

    @NotNull List<NodeModel> findAllByLeaseExpirationTimeAfter(@NotNull Instant now);
}
