package com.supr.orderservice.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.OrderCancelItem;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CancelOrderRequest {
    @NotEmpty(message = "Order id cannot be empty")
    private String orderId;

    @Valid
    List<OrderCancelItem> orderCancelItemList;

    private String reason;
}
