package dev.tiltrikt.orion.service.domain.service;

import dev.tiltrikt.orion.service.domain.model.NodeModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface NodeService {

    @NotNull List<NodeModel> getAllExpired();

    @NotNull NodeModel getById(int nodeId);

    @NotNull NodeModel save(@NotNull NodeModel nodeModel);

    @NotNull NodeModel renewLicense(int nodeId);

    void deleteAll(@NotNull List<NodeModel> modelList);

    void deleteById(int nodeId);

    @NotNull NodeModel chooseLeader();

    @NotNull Optional<NodeModel> getLeader();

    boolean existsLeader();
}
