package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.api.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.model.InstanceModel;
import dev.tiltrikt.orion.service.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceHeartbeatConsumer {

    @NotNull InstanceService instanceService;

    @NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_HEARTBEAT_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceHeartbeatEvent event) {
        InstanceModel instanceModel = instanceService.getById(event.getInstanceId());
        instanceService.renewLicense(event.getInstanceId());
        if (instanceModel.getLeaseExpirationTime().isBefore(Instant.now())) {
            RegistryUpdateEvent registryUpdateEvent = new RegistryUpdateEvent(
                    instanceModel.getServiceId(),
                    instanceModel.getHost(),
                    instanceModel.getPort(),
                    instanceModel.getMetadata()
            );
            kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instanceModel.getId(), registryUpdateEvent);
        }
    }
}
