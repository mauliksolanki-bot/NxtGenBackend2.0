package com.WF360.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WF360ApplicationStartupListener {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(WF360ApplicationStartupListener.class);

  @EventListener(ApplicationReadyEvent.class)
  public void printApplicationStartedMessage() {
    LOGGER.info("WF360 application started successfully. API base URL: http://localhost:8080/api");
  }
}
