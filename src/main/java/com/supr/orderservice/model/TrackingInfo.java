package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TrackingInfo {
    private String courierPartner;
    private String typeOfShipping;
    private String shippingCarrier;
    private String trackingId;
    private String trackingLink;
    private String deliveryDate;
    private Object[] trackingUpdates;
}
