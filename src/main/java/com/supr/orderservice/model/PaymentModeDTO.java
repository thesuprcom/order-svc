package com.supr.orderservice.model;

import com.supr.orderservice.enums.PaymentMode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentModeDTO {

    private String mode;

    private String displayValue;

    public PaymentModeDTO(PaymentMode paymentMode) {
        this.mode = paymentMode.name();
        this.displayValue = paymentMode.getDisplayText();
    }
}