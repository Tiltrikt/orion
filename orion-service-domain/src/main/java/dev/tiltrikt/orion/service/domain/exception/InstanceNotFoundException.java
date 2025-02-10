package dev.tiltrikt.orion.service.domain.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public class InstanceNotFoundException extends InstanceException {

    @NotNull List<String> missingInstances;

    public InstanceNotFoundException(@NotNull String message, Object @NotNull ... args) {
        super(message, args);
        this.missingInstances = Arrays.stream(args)
                .map(Object::toString)
                .toList();
    }
}
