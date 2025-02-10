package dev.tiltrikt.orion.service.domain.service;

import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface InstanceService {

    @NotNull List<InstanceModel> getAllExpired();

    boolean existsById(@NotNull String instanceId);

    @NotNull InstanceModel getById(@NotNull String instanceId);

    @NotNull InstanceModel save(@NotNull InstanceModel lease);

    @NotNull InstanceModel renewLicense(@NotNull String instanceId);

    void deleteAll(@NotNull List<InstanceModel> modelList);

    void deleteById(@NotNull String instanceId);

    @NotNull List<InstanceModel> findAll();

    @NotNull List<InstanceModel> saveAll(@NotNull List<InstanceModel> instanceModels);

    @NotNull List<InstanceModel> getAllById(@NotNull List<String> instanceIdList);

    void deleteAllById(@NotNull List<String> instanceIdList);
}
