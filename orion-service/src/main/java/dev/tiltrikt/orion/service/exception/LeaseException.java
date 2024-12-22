package dev.tiltrikt.orion.service.exception;

import org.jetbrains.annotations.NotNull;

public class LeaseException extends RuntimeException {

    public LeaseException(@NotNull String message, Object @NotNull ... args) {
        super(message.formatted(args));
    }
}
