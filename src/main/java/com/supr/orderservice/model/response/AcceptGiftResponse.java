package com.supr.orderservice.model.response;

import com.supr.orderservice.model.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptGiftResponse {
    private List<ItemInfo> items;
    private boolean shouldInitiateReceiverPayment;
    private String orderId;
}
