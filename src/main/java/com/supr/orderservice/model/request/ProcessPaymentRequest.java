package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.model.Address;
import com.supr.orderservice.model.CardData;
import com.supr.orderservice.model.PriceDetails;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProcessPaymentRequest implements Serializable {
    private PaymentMode paymentModeSelected;
    private String orderId;
    private Address billingAddress;
    private Boolean isRegisteredBillingAddress;
    private CardData cardData;
    private PriceDetails priceDetails;
    private Boolean isWalletApplied;
    private BigDecimal appliedWalletAmount;
}
