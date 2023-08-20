package com.supr.orderservice.model;

import lombok.Data;

@Data
public class ItemStatusChange {
    private String pskuCode;
    private String status;
}
