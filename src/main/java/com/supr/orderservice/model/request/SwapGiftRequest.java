package com.supr.orderservice.model.request;

import com.supr.orderservice.model.ItemInfo;
import lombok.Data;

import java.util.List;

@Data
public class SwapGiftRequest {
    private String orderId;
    List<ItemInfo> swappedItemInfo;
}
