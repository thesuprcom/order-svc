package com.supr.orderservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ItemStatusEnum {
  AVAILABLE("AVAILABLE"),
  DISABLE("DISABLE"),
  OUT_OF_STOCK("OUT_OF_STOCK"),
  NO_COMMISSION("NO_COMMISSION");

  private String status;

  @JsonCreator
  public static ItemStatusEnum getEnumByName(String name) {
    return Arrays.stream(ItemStatusEnum.values())
        .filter(itemStatusEnum -> !Strings.isNullOrEmpty(name) && itemStatusEnum.status.equalsIgnoreCase(name))
        .findFirst().orElse(null);
  }
}
