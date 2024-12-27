package dev.tiltrikt.orion.service.api.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceRegistrationEvent {

    @NotNull String serviceId;

    @NotNull String host;

    int port;

    int leaseDuration;

    @NotNull Map<String, String> metadata;

    public @NotNull String getInstanceId() {
        return host + ":" + port;
    }
}
