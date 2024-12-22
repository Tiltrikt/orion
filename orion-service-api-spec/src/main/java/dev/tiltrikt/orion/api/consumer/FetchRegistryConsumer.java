package dev.tiltrikt.orion.api.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.model.OrionInstance;
import dev.tiltrikt.orion.api.registry.RegistryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@KafkaListener(topics = KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC)
public class FetchRegistryConsumer {

    @NotNull RegistryRepository registryRepository;

    @KafkaHandler
    public void receive(@NotNull dev.tiltrikt.orion.api.event.RegistryUpdateEvent event) {
        OrionInstance orionInstance = new OrionInstance(
                event.getServiceId(),
                event.getHost(),
                event.getPort(),
                event.getMetadata()
        );
        registryRepository.save(orionInstance);
    }

    @KafkaHandler
    public void delete(@Payload(required = false) KafkaNull nul, @Header(KafkaHeaders.RECEIVED_KEY) String instanceId) {
        registryRepository.deleteById(instanceId);
    }
}