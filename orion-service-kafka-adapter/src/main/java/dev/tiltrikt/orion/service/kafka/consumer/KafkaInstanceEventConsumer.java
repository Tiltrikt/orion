package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.common.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.MessageListener;

import java.time.Instant;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaInstanceEventConsumer implements MessageListener<String, Object> {

    @NotNull RegistrationHandler registrationHandler;

    @NotNull DeregistrationHandler deregistrationHandler;

    @NotNull HeartbeatHandler heartbeatHandler;

    @NotNull InstanceModelFactory instanceModelFactory;

    public void register(@NotNull InstanceRegistrationEvent instanceRegistrationEvent) {
        InstanceModel instanceModel = instanceModelFactory.create(
                instanceRegistrationEvent.getInstanceId(),
                instanceRegistrationEvent.getServiceId(),
                instanceRegistrationEvent.getHost(),
                instanceRegistrationEvent.getPort(),
                instanceRegistrationEvent.getMetadata(),
                instanceRegistrationEvent.getLeaseDuration()
        );
        registrationHandler.register(instanceModel);
    }

    public void deregister(@NotNull InstanceDeregistrationEvent event) {
        deregistrationHandler.deregister(event.getInstanceId());
    }

    public void heartbeat(@NotNull InstanceHeartbeatEvent event) {
        heartbeatHandler.update(event.getInstanceId());
    }

    @Override
    public void onMessage(@NotNull ConsumerRecord<String, Object> record) {
        if (record.value() instanceof InstanceRegistrationEvent) {
            register((InstanceRegistrationEvent) record.value());
        } else if (record.value() instanceof InstanceDeregistrationEvent) {
            deregister((InstanceDeregistrationEvent) record.value());
        } else if (record.value() instanceof InstanceHeartbeatEvent) {
            heartbeat((InstanceHeartbeatEvent) record.value());
        }
    }
}