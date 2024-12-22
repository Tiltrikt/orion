package dev.tiltrikt.orion.api.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionInstance implements ServiceInstance {

    @NotNull String instanceId;

    @NotNull String serviceId;

    @NotNull String host;

    @NotNull String port;

    @NotNull Map<String, String> metadata;

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public URI getUri() {
        return URI.create("http://" + host + ":" + port);
    }

    public int getPort() {
        return Integer.parseInt(port);
    }
}
