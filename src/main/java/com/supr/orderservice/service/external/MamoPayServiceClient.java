package com.supr.orderservice.service.external;

import com.supr.orderservice.model.pg.request.MamoPayPaymentLinkRequest;
import com.supr.orderservice.model.pg.request.MamoPaySavedCardPaymentRequest;
import com.supr.orderservice.model.pg.response.MamoPayPaymentLinkResponse;
import com.supr.orderservice.model.response.MamoPayChargeDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "mamopay-pg-service", url = "${mamopay.base-url}")
public interface MamoPayServiceClient {
    @PostMapping(value = "/manage_api/v1/links", consumes = MediaType.APPLICATION_JSON_VALUE)
    MamoPayPaymentLinkResponse createPaymentLink(@RequestBody MamoPayPaymentLinkRequest mamoPayPaymentLinkRequest,
                                                 @RequestHeader("Authorization") String accessToken);

    @GetMapping(value = "/manage_api/v1/charges/{charge-id}")
    MamoPayChargeDetailsResponse fetchChargeDetails(@PathVariable("change-id") String chargeId,
                                                    @RequestHeader("Authorization") String accessToken);

    @PostMapping(value = "/manage_api/v1/charges", consumes = MediaType.APPLICATION_JSON_VALUE)
    MamoPayChargeDetailsResponse initiateSavedCardPayment(
            @RequestBody MamoPaySavedCardPaymentRequest mamoPaySavedCardPaymentRequest,
            @RequestHeader("Authorization") String accessToken);
}
