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
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.listener.MessageListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaInstanceEventConsumer implements BatchMessageListener<String, Object> {

    @NotNull RegistrationHandler registrationHandler;

    @NotNull DeregistrationHandler deregistrationHandler;

    @NotNull HeartbeatHandler heartbeatHandler;

    @NotNull InstanceModelFactory instanceModelFactory;

    public void register(@NotNull List<InstanceRegistrationEvent> instanceRegistrationEventList) {
        for (InstanceRegistrationEvent instanceRegistrationEvent : instanceRegistrationEventList) {
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
    }

    public void deregister(@NotNull List<InstanceDeregistrationEvent> eventList) {
        for (InstanceDeregistrationEvent event : eventList) {
            deregistrationHandler.deregister(event.getInstanceId());
        }
    }

    public void heartbeat(@NotNull List<InstanceHeartbeatEvent> eventList) {
        for (InstanceHeartbeatEvent event : eventList) {
            heartbeatHandler.update(event.getInstanceId());
        }
    }

    @Override
    public void onMessage(@NotNull List<ConsumerRecord<String, Object>> records) {
        if (records.isEmpty()) return;

        Map<Class<?>, List<Object>> groupedEvents = records.stream()
                .map(ConsumerRecord::value)
                .collect(Collectors.groupingBy(Object::getClass));

        List<InstanceRegistrationEvent> registrationEvents = List.of();
        List<?> regObj = groupedEvents.getOrDefault(InstanceRegistrationEvent.class, List.of());
        if (regObj != null) {
            registrationEvents = regObj.stream()
                    .filter(InstanceRegistrationEvent.class::isInstance)
                    .map(InstanceRegistrationEvent.class::cast)
                    .collect(Collectors.toList());
        }
        if (!registrationEvents.isEmpty()) {
            register(registrationEvents);
        }

        List<InstanceDeregistrationEvent> deregistrationEvents = List.of();
        List<?> deregObj = groupedEvents.getOrDefault(InstanceDeregistrationEvent.class, List.of());
        if (deregObj != null) {
            deregistrationEvents = deregObj.stream()
                    .filter(InstanceDeregistrationEvent.class::isInstance)
                    .map(InstanceDeregistrationEvent.class::cast)
                    .collect(Collectors.toList());
        }
        if (!deregistrationEvents.isEmpty()) {
            deregister(deregistrationEvents);
        }

        List<InstanceHeartbeatEvent> heartbeatEvents = List.of();
        List<?> hbObj = groupedEvents.getOrDefault(InstanceHeartbeatEvent.class, List.of());
        if (hbObj != null) {
            heartbeatEvents = hbObj.stream()
                    .filter(InstanceHeartbeatEvent.class::isInstance)
                    .map(InstanceHeartbeatEvent.class::cast)
                    .collect(Collectors.toList());
        }
        if (!heartbeatEvents.isEmpty()) {
            heartbeat(heartbeatEvents);
        }

    }
}