package com.supr.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 8219094392316856723L;

  public OrderNotFoundException(String message) {
    super(message);
  }
}
