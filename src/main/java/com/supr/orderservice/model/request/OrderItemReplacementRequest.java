package com.supr.orderservice.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class OrderItemReplacementRequest implements Serializable {
  @NotEmpty(message = "Replace Order Item's Item Id cannot be empty")
  private String itemId;
  @Positive(message = "Replacement Order Item quantity should be positive")
  private BigDecimal quantity;
  @Positive(message = "Replacement Order Item Price should be positive")
  private BigDecimal price;
}
