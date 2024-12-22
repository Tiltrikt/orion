package dev.tiltrikt.orion.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@Table(name = "lease")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@SuppressWarnings("JpaDataSourceORMInspection")
public class LeaseModel {

    @Id
    @NotNull String id;

    int leaseDuration;

    @NonFinal
    @NotNull Instant leaseExpirationTime;
}
