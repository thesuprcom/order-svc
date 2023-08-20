package com.supr.orderservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {

  private String itemId;

  private OperationType operationType;

  private BigDecimal operationValue;

  private String source = "OMS";

  public enum OperationType {
    DEC,
    INC
  }

  private Boolean isSlottedQuantityUpdate = false;
}
