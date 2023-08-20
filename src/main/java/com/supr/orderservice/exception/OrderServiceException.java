package com.supr.orderservice.exception;

import com.supr.orderservice.enums.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class OrderServiceException extends RuntimeException {

  private ErrorEnum errorEnum;
  private HttpStatus httpStatus;

  public OrderServiceException(String string) {
    super(string);
  }

  public OrderServiceException(ErrorEnum errorEnum) {
    super(errorEnum.getErrorMessage());
    this.errorEnum = errorEnum;
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public OrderServiceException(ErrorEnum errorEnum, HttpStatus status) {
    super(errorEnum.getErrorMessage());
    this.errorEnum = errorEnum;
    this.httpStatus = status;
  }

}
