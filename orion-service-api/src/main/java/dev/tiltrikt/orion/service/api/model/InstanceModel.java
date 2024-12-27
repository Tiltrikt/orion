package dev.tiltrikt.orion.service.api.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@Table(name = "lease")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@SuppressWarnings("JpaDataSourceORMInspection")
public class InstanceModel {

    @Id
    @NotNull String id;

    @NotNull String serviceId;

    @NotNull String host;

    int port;

    @ElementCollection
    @CollectionTable(name = "entity_metadata", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value")
    @NotNull Map<String, String> metadata;

    int leaseDuration;

    @NonFinal
    @NotNull Instant leaseExpirationTime;
}
