package dev.tiltrikt.orion.service.domain.model;

import dev.tiltrikt.orion.common.instance.InstanceState;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;

@ToString
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "instance")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
public class InstanceModel {

    @Id
    @NotNull String id;

    @NotNull String serviceId;

    @NotNull String host;

    int port;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "entity_metadata", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value")
    @NotNull Map<String, String> metadata;

    int leaseDuration;

    @NotNull Instant leaseExpirationTime;

    @Enumerated(EnumType.STRING)
    @NotNull InstanceState state;
}