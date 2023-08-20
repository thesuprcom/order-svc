package com.supr.orderservice.enums;

import lombok.Getter;

public enum InvoiceCycle {
    SEVEN_DAYS("7"),
    FOURTEEN_DAYS("14"),
    TWENTY_EIGHT_DAYS("28");

    @Getter
    private String days;

    InvoiceCycle(String days) {
        this.days = days;
    }

}