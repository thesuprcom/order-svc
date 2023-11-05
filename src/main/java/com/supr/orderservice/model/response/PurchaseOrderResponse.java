package com.supr.orderservice.model.response;

import com.supr.orderservice.model.CardDetails;
import com.supr.orderservice.model.SavedCardDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderResponse {

    private List<CardDetails> savedCards;

    private String orderId;

    private BigDecimal amountPayable;
}
