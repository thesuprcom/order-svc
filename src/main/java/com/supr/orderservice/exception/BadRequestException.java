package com.supr.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
  private static final long serialVersionUID = 8219094392316856723L;

  public BadRequestException(String message) {
    super(message);
  }
}
