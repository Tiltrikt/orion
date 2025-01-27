package dev.tiltrikt.orion.service.domain.service;

import dev.tiltrikt.orion.service.domain.exception.InstanceException;
import dev.tiltrikt.orion.service.domain.exception.NodeException;
import dev.tiltrikt.orion.service.domain.model.NodeModel;
import dev.tiltrikt.orion.service.domain.repository.NodeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class NodeServiceImpl implements NodeService {

    @NotNull NodeRepository repository;

    @Override
    @Unmodifiable
    public @NotNull List<NodeModel> getAllExpired() {
        return List.copyOf(repository.findAllByLeaseExpirationTimeBefore(Instant.now()));
    }

    @Override
    public @NotNull NodeModel getById(int nodeId) {
        return repository.findById(nodeId)
                .orElseThrow(() -> new InstanceException("Node '%s' not exists", nodeId));
    }

    @Override
    public @NotNull NodeModel save(@NotNull NodeModel nodeModel) {
        return repository.save(nodeModel);
    }

    @Override
    public void deleteAll(@NotNull List<NodeModel> modelList) {
        repository.deleteAllInBatch(modelList);
    }

    @Override
    public void deleteById(int nodeId) {
        repository.deleteById(nodeId);
    }

    @Override
    public @NotNull NodeModel chooseLeader() {
        return repository.findAllByLeaseExpirationTimeAfter(Instant.now()).stream()
                .max(Comparator.comparingInt(NodeModel::getId))
                .get();
    }

    @Override
    public @NotNull Optional<NodeModel> getLeader() {
        return repository.findAll().stream().filter(NodeModel::isLeader).findFirst();
    }

    @Override
    public @NotNull NodeModel renewLicense(int nodeId) {
        NodeModel nodeModel = repository.findById(nodeId)
                .orElseThrow(() -> new NodeException("Node '%s' not exists", nodeId));
        nodeModel.setLeaseExpirationTime(Instant.now().plusSeconds(nodeModel.getLeaseDuration()));
        return repository.save(nodeModel);
    }
}
