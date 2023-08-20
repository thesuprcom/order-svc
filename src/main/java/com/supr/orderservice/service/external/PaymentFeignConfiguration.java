package com.supr.orderservice.service.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;

public class PaymentFeignConfiguration {
  @Bean
  public JacksonEncoder encoder() {
    // Uses lower camel casing by default
    return new JacksonEncoder(new ObjectMapper());
  }

  @Bean
  public JacksonDecoder decoder() {
    // Uses lower camel casing by default
    return new JacksonDecoder(new ObjectMapper());
  }

  @Bean
  public ErrorDecoder errorDecoder(JacksonDecoder decoder) {
    return new PaymentFeignErrorDecoder(decoder);
  }
}
