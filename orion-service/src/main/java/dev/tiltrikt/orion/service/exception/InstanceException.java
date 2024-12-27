package dev.tiltrikt.orion.service.exception;

import org.jetbrains.annotations.NotNull;

public class InstanceException extends RuntimeException {

    public InstanceException(@NotNull String message, Object @NotNull ... args) {
        super(message.formatted(args));
    }
}
