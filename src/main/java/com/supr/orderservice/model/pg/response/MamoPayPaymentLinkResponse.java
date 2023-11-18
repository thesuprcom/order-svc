package com.supr.orderservice.model.pg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.pg.MamoPayRules;
import com.supr.orderservice.model.pg.MamoPaySubscription;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MamoPayPaymentLinkResponse implements Serializable {
    private String id;
    private String title;
    private String description;
    private int capacity;
    private boolean active;
    private String returnUrl;
    private String failureReturnUrl;
    private double amount;
    private boolean sendCustomerReceipt;
    private String paymentUrl;
    private MamoPayRules rules;
    private MamoPaySubscription subscription;
    private HashMap<String, String> customData;
    private String externalId;
}
