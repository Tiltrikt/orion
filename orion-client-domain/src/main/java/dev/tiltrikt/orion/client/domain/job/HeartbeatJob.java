package dev.tiltrikt.orion.client.domain.job;

import dev.tiltrikt.orion.client.common.publisher.EventPublisher;
import dev.tiltrikt.orion.client.domain.model.OrionInstance;
import dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatJob {

    @NotNull EventPublisher eventPublisher;

    @NotNull OrionInstance thisOrionInstance;

    @Scheduled(
            timeUnit = TimeUnit.SECONDS,
            initialDelayString = "${orion.client.heartbeat-rate-sec}",
            fixedRateString = "${orion.client.heartbeat-rate-sec}"
    )
    public void execute() {
        InstanceHeartbeatEvent instanceHeartbeatEvent = new InstanceHeartbeatEvent(thisOrionInstance.getInstanceId());
        eventPublisher.publishHeartbeat(instanceHeartbeatEvent);
    }
}