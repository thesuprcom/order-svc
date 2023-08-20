package com.supr.orderservice.service.external;

import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.model.response.PaymentOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "noon-payments", url = "${payment.gateway.base-url}", configuration =
        {PaymentFeignConfiguration.class})
public interface PaymentGatewayServiceClient {

  @PostMapping(value = "/payment/v1/order", consumes = "application/json", produces = "application/json")
  PaymentGatewayResponse processNoonOrder(@RequestHeader("Authorization") String authorization,
                                          @RequestBody PaymentOrderResponse request);

  @GetMapping("/payment/v1/order/{orderId}")
  PaymentGatewayResponse getOrder(@RequestHeader("Authorization") String authorization, @PathVariable("orderId") Long orderId);

  @Async
  @DeleteMapping("/payment/v1/token/{tokenId}")
  void deleteToken(@RequestHeader("Authorization") String authorization, @PathVariable("tokenId") String tokenId);
}
