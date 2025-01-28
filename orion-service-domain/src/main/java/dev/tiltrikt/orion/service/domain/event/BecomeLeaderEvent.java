package dev.tiltrikt.orion.service.domain.event;

import org.springframework.context.ApplicationEvent;

public class BecomeLeaderEvent extends ApplicationEvent {

    public BecomeLeaderEvent(Object source) {
        super(source);
    }
}
