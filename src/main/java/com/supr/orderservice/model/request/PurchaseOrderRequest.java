package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.Address;
import com.supr.orderservice.model.SellerInfo;
import com.supr.orderservice.model.UserCartDTO;
import com.supr.orderservice.model.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PurchaseOrderRequest implements Serializable {
    //@JsonUnwrapped
    private UserCartDTO userCartDTO;
    private String orderId;
    private String countryCode;
    private String currencyCode;
    private String ipAddress;
    private boolean isScheduledOrder;
    private String scheduleDate;
}
