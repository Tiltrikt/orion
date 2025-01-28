package dev.tiltrikt.orion.common.event;

import dev.tiltrikt.orion.common.instance.InstanceState;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;

@ToString
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplicationEvent {

    @NotNull String instanceId;

    @NotNull String serviceId;

    @NotNull String host;

    int port;

    int leaseDuration;

    @NotNull Map<String, String> metadata;
    @NotNull Instant leaseExpirationTime;

    @NotNull InstanceState instanceState;

    public @NotNull String getInstanceId() {
        return host + ":" + port;
    }
}
