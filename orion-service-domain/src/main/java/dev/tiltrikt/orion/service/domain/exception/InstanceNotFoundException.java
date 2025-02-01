package dev.tiltrikt.orion.service.domain.exception;

import org.jetbrains.annotations.NotNull;

public class InstanceNotFoundException extends InstanceException {

    public InstanceNotFoundException(@NotNull String message, Object @NotNull ... args) {
        super(message, args);
    }
}
