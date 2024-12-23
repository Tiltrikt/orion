package dev.tiltrikt.orion.api.job;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceHeartbeatEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatJob {

    @NotNull KafkaTemplate<String, InstanceHeartbeatEvent> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void execute() {
        InstanceHeartbeatEvent instanceHeartbeatEvent = new InstanceHeartbeatEvent("localhost:8081");
        kafkaTemplate.send(KafkaTopicConfiguration.INSTANCE_HEARTBEAT_TOPIC, instanceHeartbeatEvent);
    }
}
