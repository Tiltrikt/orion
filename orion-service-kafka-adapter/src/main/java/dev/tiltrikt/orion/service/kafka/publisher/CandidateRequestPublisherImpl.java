package dev.tiltrikt.orion.service.kafka.publisher;//package dev.tiltrikt.orion.service.domain.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.service.common.publisher.CandidateRequestPublisher;
import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateRequestPublisherImpl implements CandidateRequestPublisher {

    @NotNull KafkaTemplate<String, CandidateRequestEvent> kafkaTemplate;

    @Override
    public void publish(@NotNull CandidateRequestEvent event) {
        kafkaTemplate.send(KafkaTopicConfiguration.NODE_EVENT_TOPIC, event);
    }
}
