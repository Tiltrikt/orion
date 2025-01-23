package dev.tiltrikt.orion.client.common.publisher;

import dev.tiltrikt.orion.common.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.common.event.InstanceRegistrationEvent;
import org.jetbrains.annotations.NotNull;

public interface EventPublisher {

    void publishRegistration(@NotNull InstanceRegistrationEvent event);

    void publishDeregistration(@NotNull InstanceDeregistrationEvent event);

    void publishHeartbeat(@NotNull InstanceHeartbeatEvent event);
}
