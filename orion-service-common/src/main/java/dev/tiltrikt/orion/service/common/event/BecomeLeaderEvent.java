package dev.tiltrikt.orion.service.common.event;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

public class BecomeLeaderEvent extends ApplicationEvent {

    public BecomeLeaderEvent(@NotNull Object source) {
        super(source);
    }
}
