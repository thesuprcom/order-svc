package com.supr.orderservice.exception;

import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PaymentProcessingException extends RuntimeException {
  private final ErrorEnum errorEnum;
  private final HttpStatus httpStatus;
  private final Optional<PaymentGatewayResponse> noonResponse;

  public PaymentProcessingException(ErrorEnum errorEnum,
                                    HttpStatus status,
                                    PaymentGatewayResponse noonResponse) {
    super(errorEnum.getErrorMessage());
    this.errorEnum = errorEnum;
    this.httpStatus = status;
    this.noonResponse = Optional.ofNullable(noonResponse);
  }

  public PaymentProcessingException(ErrorEnum errorEnum, HttpStatus status) {
    super(errorEnum.getErrorMessage());
    this.errorEnum = errorEnum;
    this.httpStatus = status;
    this.noonResponse = Optional.empty();
  }
}
