package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.service.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.api.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.service.api.handler.heartbeat.HeartbeatHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceHeartbeatConsumer {

    @NotNull HeartbeatHandler heartbeatHandler;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_HEARTBEAT_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceHeartbeatEvent event) {
        heartbeatHandler.update(event.getInstanceId());
    }
}
