package dev.tiltrikt.orion.api.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Instance {

    @NotNull String instanceId;

    @NotNull String serviceId;

    @NotNull String host;

    @NotNull String port;

    @NotNull Map<String, String> metadata;
}
