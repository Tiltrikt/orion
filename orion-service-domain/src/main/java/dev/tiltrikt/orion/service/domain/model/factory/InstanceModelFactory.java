package dev.tiltrikt.orion.service.domain.model.factory;

import dev.tiltrikt.orion.common.instance.InstanceState;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class InstanceModelFactory {

    public @NotNull InstanceModel create(
            @NotNull String instanceId,
            @NotNull String serviceId,
            @NotNull String host,
            int port,
            @NotNull Map<String, String> metadata,
            int leaseDuration) {
        return new InstanceModel(
                instanceId,
                serviceId,
                host,
                port,
                metadata,
                leaseDuration,
                Instant.now().plusSeconds(leaseDuration),
                InstanceState.UP
        );
    }

    public @NotNull InstanceModel createWithUnknownState(@NotNull String instanceId) {
        return new InstanceModel(
                instanceId,
                "unknown",
                "unknown",
                -1,
                new HashMap<>(),
                120,
                Instant.now().plusSeconds(120),
                InstanceState.UNKNOWN
        );
    }
}
