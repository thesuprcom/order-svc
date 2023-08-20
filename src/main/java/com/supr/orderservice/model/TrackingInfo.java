package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingInfo {
    private String courierPartner;
    private String typeOfShipping;
    private String shippingCarrier;
    private String trackingId;
    private String trackingLink;
    private String deliveryDate;
    private Object[] trackingUpdates;
}
