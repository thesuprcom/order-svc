package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMode {
    PAY_ONLINE("Credit/Debit Card"),

    CashOnDelivery("Cash on Delivery"),
    Card("Card");

    private String displayText;
}