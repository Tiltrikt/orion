package dev.tiltrikt.orion.api.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.net.URI;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionInstance implements Registration {

    @NotNull String serviceId;

    @NotNull String host;

    int port;

    @NotNull Map<String, String> metadata;

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public @NotNull String getInstanceId() {
        return host + ":" + port;
    }

    @Override
    public URI getUri() {
        return URI.create("http://" + host + ":" + port);
    }
}
