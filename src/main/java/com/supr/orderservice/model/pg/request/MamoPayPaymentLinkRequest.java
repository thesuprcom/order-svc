package com.supr.orderservice.model.pg.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.pg.MamoPayRules;
import com.supr.orderservice.model.pg.MamoPaySubscription;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MamoPayPaymentLinkRequest implements Serializable {
    private String title;
    private String description;
    private int capacity;
    private boolean active;
    private String returnUrl;
    private String failureReturnUrl;
    private int processingFeePercentage;
    private double amount;
    private String amountCurrency;
    private boolean isWidget;
    private boolean enableTabby;
    private boolean enableMessage;
    private boolean enableTips;
    private String saveCard;
    private boolean enableCustomerDetails;
    private boolean enableQuantity;
    private boolean enableQrCode;
    private boolean sendCustomerReceipt;
    private MamoPayRules rules;
    private MamoPaySubscription subscription;
    private HashMap<String, String> customData;
    private String firstName;
    private String lastName;
    private String email;
    private String externalId;
}
