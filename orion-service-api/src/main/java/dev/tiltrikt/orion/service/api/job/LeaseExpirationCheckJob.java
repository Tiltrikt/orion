package dev.tiltrikt.orion.service.api.job;

import dev.tiltrikt.orion.service.api.model.InstanceModel;
import dev.tiltrikt.orion.service.api.publisher.EventPublisher;
import dev.tiltrikt.orion.service.api.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaseExpirationCheckJob {

    @NotNull EventPublisher eventPublisher;

    @NotNull InstanceService instanceService;

    @Scheduled(fixedRate = 5000)
    public void execute() {
        List<InstanceModel> instanceModelList = instanceService.getAllExpired();
        for (InstanceModel instanceModel : instanceModelList) {
            eventPublisher.publishDeregistration(instanceModel.getId());
        }
    }
}
