package dev.tiltrikt.orion.client.kafka.consumer;

import dev.tiltrikt.orion.client.domain.model.OrionInstance;
import dev.tiltrikt.orion.client.domain.repository.RegistryRepository;
import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@KafkaListener(
        topics = KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC,
        groupId = "${spring.application.name}"
)
public class FetchRegistryConsumer implements ConsumerSeekAware {

    @NotNull RegistryRepository registryRepository;

    @KafkaHandler
    public void receive(@NotNull RegistryUpdateEvent event) {
        OrionInstance orionInstance = new OrionInstance(
                event.getServiceId(),
                event.getHost(),
                event.getPort(),
                event.getMetadata()
        );
        registryRepository.save(orionInstance);
    }

    @KafkaHandler
    @SuppressWarnings("unused")
    public void delete(@Payload(required = false) KafkaNull nul, @Header(KafkaHeaders.RECEIVED_KEY) String instanceId) {
        registryRepository.deleteById(instanceId);
    }

    @Override
    public void onPartitionsAssigned(
            @NotNull Map<TopicPartition, Long> assignments,
            @NotNull ConsumerSeekAware.ConsumerSeekCallback callback
    ) {
        for (TopicPartition topicPartition : assignments.keySet()) {
            if (topicPartition.topic().equals(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC)) {
                callback.seekToBeginning(topicPartition.topic(), topicPartition.partition());
            }
        }
    }
}