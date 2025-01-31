package dev.tiltrikt.orion.service.kafka.publisher;//package dev.tiltrikt.orion.service.domain.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.LeaderHeartbeatPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaderHeartbeatPublisherImpl implements LeaderHeartbeatPublisher {

    @NotNull KafkaTemplate<String, LeaderHeartbeatEvent> kafkaTemplate;

    @Override
    public void publish(@NotNull LeaderHeartbeatEvent event) {
        kafkaTemplate.send(KafkaTopicConfiguration.NODE_EVENT_TOPIC, event);
    }
}
