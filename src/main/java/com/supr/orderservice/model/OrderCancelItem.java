package com.supr.orderservice.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderCancelItem implements Serializable {
    @NotEmpty(message = "Item order id cannot be empty")
    private String itemOrderId;
    private String reason;
}
