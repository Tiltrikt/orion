package dev.tiltrikt.orion.service.kafka.publisher;//package dev.tiltrikt.orion.service.domain.publisher;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import dev.tiltrikt.orion.service.common.publisher.CandidateRequestPublisher;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaCandidateRequestPublisher implements CandidateRequestPublisher {

    @NotNull KafkaTemplate<String, CandidateRequestEvent> kafkaTemplate;

    @NotNull OrionServiceNode thisOrionServiceNode;

    @Override
    public void publish(@NotNull CandidateRequestEvent event) {
        log.info("Publishing candidate request: {}", event);
        kafkaTemplate.send(KafkaTopicConfiguration.NODE_EVENT_TOPIC, String.valueOf(thisOrionServiceNode.getId()), event);
    }
}
