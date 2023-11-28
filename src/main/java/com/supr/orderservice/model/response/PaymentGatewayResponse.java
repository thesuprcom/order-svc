package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.model.Result;
import com.supr.orderservice.model.pg.response.MamoPayPaymentLinkResponse;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentGatewayResponse implements Serializable {
    private MamoPayPaymentLinkResponse response;
    private MamoPayChargeDetailsResponse chargeDetailsResponse;

}
