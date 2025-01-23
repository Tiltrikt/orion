package dev.tiltrikt.orion.api.job;

import dev.tiltrikt.orion.api.model.OrionInstance;
import dev.tiltrikt.orion.service.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.api.event.InstanceHeartbeatEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatJob {

    @NotNull KafkaTemplate<String, InstanceHeartbeatEvent> kafkaTemplate;

    @NotNull OrionInstance thisOrionInstance;

    @Scheduled(
            timeUnit = TimeUnit.SECONDS,
            initialDelayString = "${orion.client.heartbeat-rate-sec}",
            fixedRateString = "${orion.client.heartbeat-rate-sec}"
    )
    public void execute() {
        InstanceHeartbeatEvent instanceHeartbeatEvent = new InstanceHeartbeatEvent(thisOrionInstance.getInstanceId());
        kafkaTemplate.send(KafkaTopicConfiguration.INSTANCE_HEARTBEAT_TOPIC, instanceHeartbeatEvent);
    }
}