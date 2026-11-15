package com.nxtgen.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NxtGenApplicationStartupListener {

    @EventListener(ApplicationReadyEvent.class)
    public void printApplicationStartedMessage() {
        System.out.println("NxtGen application started successfully at http://localhost:8080/api");
    }
}
