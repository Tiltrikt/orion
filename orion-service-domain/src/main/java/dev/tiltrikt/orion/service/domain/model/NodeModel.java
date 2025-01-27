package dev.tiltrikt.orion.service.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@ToString
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "node")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
public class NodeModel {

    @Id
    int id;

    int leaseDuration;

    @NonFinal
    @NotNull Instant leaseExpirationTime;

    @NonFinal
    boolean isLeader;
}
