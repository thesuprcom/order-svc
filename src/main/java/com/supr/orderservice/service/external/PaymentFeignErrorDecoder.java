package com.supr.orderservice.service.external;

import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.exception.PaymentProcessingException;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
public class PaymentFeignErrorDecoder implements ErrorDecoder {
  private final JacksonDecoder decoder;

  @Override
  public Exception decode(String methodKey, Response response) {
    PaymentGatewayResponse noonResponse = getPaymentGatewayResponse(response);

    return new PaymentProcessingException(ErrorEnum.PAYMENT_FAILED_FOR_CUSTOMER,
        HttpStatus.FORBIDDEN,
        noonResponse);
  }

  @SneakyThrows
  private PaymentGatewayResponse getPaymentGatewayResponse(final Response response) {
    PaymentGatewayResponse noonResponse = null;

    try {
      noonResponse = (PaymentGatewayResponse) decoder.decode(response, PaymentGatewayResponse.class);
      log.debug("Error from noon payments. Response: {}", noonResponse);
    } catch (Exception exception) {
      String responseBody = response.body().asInputStream().toString();
      // Swallow the exception
      log.error("Error while paring the noon payment response: {}", responseBody);
    }

    return noonResponse;
  }
}
