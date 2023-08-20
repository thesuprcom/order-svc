package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.ViewGiftItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViewGiftResponse implements Serializable {
    private String senderName;
    private List<ViewGiftItem> items;
    private String orderId;
}
