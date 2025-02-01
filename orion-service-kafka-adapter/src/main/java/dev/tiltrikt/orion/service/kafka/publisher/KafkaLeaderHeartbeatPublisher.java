package dev.tiltrikt.orion.service.kafka.publisher;//package dev.tiltrikt.orion.service.domain.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.LeaderHeartbeatPublisher;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaLeaderHeartbeatPublisher implements LeaderHeartbeatPublisher {

    @NotNull KafkaTemplate<String, LeaderHeartbeatEvent> kafkaTemplate;

    @NotNull OrionServiceNode thisOrionServiceNode;

    @Override
    public void publish(@NotNull LeaderHeartbeatEvent event) {
        log.info("Publishing heartbeat: {}", event);
        kafkaTemplate.send(KafkaTopicConfiguration.NODE_EVENT_TOPIC, String.valueOf(thisOrionServiceNode.getId()), event);
    }
}
