package dev.tiltrikt.orion.service.common.event;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

public class BecomeFollowerEvent extends ApplicationEvent {

    public BecomeFollowerEvent(@NotNull Object source) {
        super(source);
    }
}
