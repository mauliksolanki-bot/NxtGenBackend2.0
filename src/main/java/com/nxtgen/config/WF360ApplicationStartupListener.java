package com.WF360.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WF360ApplicationStartupListener {

    @EventListener(ApplicationReadyEvent.class)
    public void printApplicationStartedMessage() {
        System.out.println("WF360 application started successfully at http://localhost:8080/api");
    }
}
