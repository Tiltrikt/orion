package dev.tiltrikt.orion.service.job;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.model.InstanceModel;
import dev.tiltrikt.orion.service.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaseExpirationCheckJob {

    @NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate;

    @NotNull InstanceService instanceService;

    @Scheduled(fixedRate = 5000)
    public void execute() {
        List<InstanceModel> instanceModelList = instanceService.getAllExpired();
        for (InstanceModel instanceModel : instanceModelList) {
            kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, instanceModel.getId(), null);
        }
    }
}
