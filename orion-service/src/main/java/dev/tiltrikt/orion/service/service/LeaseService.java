package dev.tiltrikt.orion.service.service;

import dev.tiltrikt.orion.service.model.LeaseModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface LeaseService {

    @NotNull List<LeaseModel> getAllExpired();

    @NotNull LeaseModel save(@NotNull LeaseModel lease);

    @NotNull LeaseModel renewLicense(@NotNull String instanceId);

    void deleteAll(@NotNull List<LeaseModel> modelList);

    void deleteById(@NotNull String instanceId);
}
