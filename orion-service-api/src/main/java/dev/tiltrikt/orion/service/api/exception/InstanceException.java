package dev.tiltrikt.orion.service.api.exception;

import org.jetbrains.annotations.NotNull;

public class InstanceException extends RuntimeException {

    public InstanceException(@NotNull String message, Object @NotNull ... args) {
        super(message.formatted(args));
    }
}
