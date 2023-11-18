package com.supr.orderservice.service.external;

import com.supr.orderservice.model.pg.request.MamoPayPaymentLinkRequest;
import com.supr.orderservice.model.pg.response.MamoPayPaymentLinkResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "mamopay-pg-service", url = "${mamopay.base-url}")
public interface MamoPayServiceClient {
    @PostMapping(value = "/manage_api/v1/links", consumes = MediaType.APPLICATION_JSON_VALUE)
    MamoPayPaymentLinkResponse createPaymentLink(@RequestBody MamoPayPaymentLinkRequest mamoPayPaymentLinkRequest,
                                                 @RequestHeader("Authorization") String accessToken);
}
