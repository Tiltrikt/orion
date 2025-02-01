package dev.tiltrikt.orion.service.domain.exception;

import org.jetbrains.annotations.NotNull;

public class NodeException extends RuntimeException {

    public NodeException(@NotNull String message, Object @NotNull ... args) {
        super(message.formatted(args));
    }

}
