package com.supr.orderservice.model;

import lombok.Data;

@Data
public class ItemChangeDto {
    private String pskuCode;
    private String itemCode;
    private int quantity;
    private TrackingInfo itemTrackingInfo;

}
