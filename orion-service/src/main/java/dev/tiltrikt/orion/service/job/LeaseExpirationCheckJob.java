package dev.tiltrikt.orion.service.job;

import dev.tiltrikt.orion.api.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.api.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.model.LeaseModel;
import dev.tiltrikt.orion.service.service.LeaseService;
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

    @NotNull LeaseService leaseService;

    @Scheduled(fixedRate = 5000)
    public void execute() {
        List<LeaseModel> leaseModelList = leaseService.getAllExpired();
        for (LeaseModel leaseModel : leaseModelList) {
            kafkaTemplate.send(KafkaTopicConfiguration.FETCH_REGISTRY_TOPIC, leaseModel.getId(), null);
        }
        leaseService.deleteAll(leaseModelList);
    }
}
