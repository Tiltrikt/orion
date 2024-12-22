package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.service.service.LeaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceHeartbeatConsumer {

    @NotNull LeaseService leaseService;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_HEARTBEAT_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceHeartbeatEvent event) {
        System.out.println(event);
        leaseService.renewLicense(event.getInstanceId());

    }
}
