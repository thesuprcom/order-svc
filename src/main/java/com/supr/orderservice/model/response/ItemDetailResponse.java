package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.model.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDetailResponse  implements Serializable {
    private ItemInfo itemInfo;
    private String orderId;
}
