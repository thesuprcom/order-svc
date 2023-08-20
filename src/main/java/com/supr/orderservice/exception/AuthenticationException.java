package com.supr.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {
  private static final long serialVersionUID = 8209094292216256722L;

  public AuthenticationException(String message) {
    super(message);
  }
}
