package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.api.model.Instance;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InstanceRegistrationConsumer {

    KafkaTemplate<String, Instance> kafkaTemplate;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_REGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceRegistrationEvent event) {
        Instance instance = new Instance(
                event.getInstanceId(),
                event.getServiceId(),
                event.getHost(),
                event.getPort(),
                event.getMetadata()
        );
        kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instance.getInstanceId(), instance).join();
    }
}
