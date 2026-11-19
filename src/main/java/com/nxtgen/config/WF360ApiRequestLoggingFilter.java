package com.WF360.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class WF360ApiRequestLoggingFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(WF360ApiRequestLoggingFilter.class);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    long startedAt = System.nanoTime();
    try {
      filterChain.doFilter(request, response);
    } finally {
      long elapsedMilliseconds = (System.nanoTime() - startedAt) / 1_000_000;
      int status = response.getStatus();
      if (status < HttpServletResponse.SC_BAD_REQUEST) {
        LOGGER.info(
            "http_request method={} path={} status={} duration_ms={}",
            request.getMethod(),
            request.getRequestURI(),
            status,
            elapsedMilliseconds);
      } else {
        LOGGER.warn(
            "http_request method={} path={} status={} duration_ms={}",
            request.getMethod(),
            request.getRequestURI(),
            status,
            elapsedMilliseconds);
      }
    }
  }
}
