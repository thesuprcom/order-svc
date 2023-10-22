package com.supr.orderservice.filter;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {
  private final List<String> excludedUrl = Lists.newArrayList("/healthcheck");

  public RequestLoggingFilter() {
    this.setIncludeHeaders(true);
    this.setIncludePayload(true);
    this.setIncludeQueryString(true);
    this.setMaxPayloadLength(10485760); // 10 MB
  }

  @Override
  protected void beforeRequest(HttpServletRequest request, String message) {
    log(request, message);
  }

  @Override
  protected void afterRequest(HttpServletRequest request, String message) {
    log(request, message);
  }

  private void log(HttpServletRequest request, String message) {
    if (shouldLog(request.getServletPath())) {
      logger.info(message);
    }
  }

  private boolean shouldLog(String currentUrlPath) {
    return excludedUrl.stream().noneMatch(currentUrlPath::contains);
  }
}