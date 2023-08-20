package com.supr.orderservice.enums;

import lombok.Getter;

public enum OrderType {

  SENDER("Sender"),
  RECEIVER("Receiver");

  @Getter
  private String prefix;

  OrderType(String prefix) {
    this.prefix = prefix;
  }
}
