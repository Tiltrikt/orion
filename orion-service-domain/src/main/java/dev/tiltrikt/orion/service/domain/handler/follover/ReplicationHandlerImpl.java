package dev.tiltrikt.orion.service.domain.handler.follover;

import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplicationHandlerImpl implements ReplicationHandler {

    @NotNull InstanceService instanceService;

    @Override
    public void replicate(@NotNull InstanceModel model) {
        instanceService.save(model);
    }
}