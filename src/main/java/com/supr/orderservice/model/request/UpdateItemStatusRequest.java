package com.supr.orderservice.model.request;

import com.supr.orderservice.enums.ItemStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class UpdateItemStatusRequest implements Serializable {

  private String itemId;

  private BigDecimal quantity;

  private String source;

  private ItemStatusEnum itemStatusEnum;

}
