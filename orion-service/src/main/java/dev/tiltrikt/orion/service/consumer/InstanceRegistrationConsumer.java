package dev.tiltrikt.orion.service.consumer;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.api.model.Instance;
import dev.tiltrikt.orion.service.model.LeaseModel;
import dev.tiltrikt.orion.service.model.LeaseModelFactory;
import dev.tiltrikt.orion.service.service.LeaseService;
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

    @NotNull KafkaTemplate<String, Instance> kafkaTemplate;

    @NotNull LeaseService leaseService;

    @NotNull LeaseModelFactory leaseModelFactory;

    @KafkaListener(topics = KafkaTopicConfiguration.INSTANCE_REGISTRATION_TOPIC, groupId = "orion-service")
    public void receive(@NotNull InstanceRegistrationEvent event) {
        Instance instance = new Instance(
                event.getInstanceId(),
                event.getServiceId(),
                event.getHost(),
                event.getPort(),
                event.getMetadata()
        );
        kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instance.getInstanceId(), instance);
        LeaseModel leaseModel = leaseModelFactory.create(event.getInstanceId(), event.getLeaseDuration());
        leaseService.save(leaseModel);
    }
}
