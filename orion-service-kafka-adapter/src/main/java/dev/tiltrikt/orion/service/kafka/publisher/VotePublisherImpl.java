package dev.tiltrikt.orion.service.kafka.publisher;//package dev.tiltrikt.orion.service.domain.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.common.event.VoteEvent;
import dev.tiltrikt.orion.service.common.publisher.VotePublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VotePublisherImpl implements VotePublisher {

    @NotNull KafkaTemplate<String, VoteEvent> kafkaTemplate;

    @Override
    public void publish(@NotNull VoteEvent event) {
        kafkaTemplate.send(KafkaTopicConfiguration.NODE_EVENT_TOPIC, event);
    }
}
