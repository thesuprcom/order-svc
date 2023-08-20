package com.supr.orderservice.model.response;

import com.supr.orderservice.model.SavedCardDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderResponse {

    private SavedCardDetails savedCardDetails;

    private String orderId;

    private BigDecimal amountPayable;
}
