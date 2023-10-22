package com.supr.orderservice.exception;

import lombok.Data;

@Data
public class PortalAuthExceptionResponse {

    private final String errorCode;

    private final String errorMessage;
}
