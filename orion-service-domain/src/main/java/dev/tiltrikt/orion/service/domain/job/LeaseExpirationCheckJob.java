package dev.tiltrikt.orion.service.domain.job;

import dev.tiltrikt.orion.service.common.publisher.EventPublisher;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

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
