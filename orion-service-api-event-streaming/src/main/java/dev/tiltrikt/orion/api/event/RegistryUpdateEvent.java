package dev.tiltrikt.orion.api.event;

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
public class RegistryUpdateEvent {

    @NotNull String instanceId;

    @NotNull String serviceId;

    @NotNull String host;

    @NotNull String port;

    @NotNull Map<String, String> metadata;
}
