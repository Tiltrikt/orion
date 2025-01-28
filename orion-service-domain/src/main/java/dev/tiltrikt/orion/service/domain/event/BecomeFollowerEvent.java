package dev.tiltrikt.orion.service.domain.event;

import org.springframework.context.ApplicationEvent;

public class BecomeFollowerEvent extends ApplicationEvent {

    public BecomeFollowerEvent(Object source) {
        super(source);
    }
}
