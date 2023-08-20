package com.supr.orderservice.model.response;

import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.OrderPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverPlaceOrderResponse {
    private boolean shouldTriggerReceiverPayment;
    private List<ItemInfo> items;
    private String swappedProductAmount;
    private String subTotalAmount;
    private String vat;
    private String totalPrice;

}
