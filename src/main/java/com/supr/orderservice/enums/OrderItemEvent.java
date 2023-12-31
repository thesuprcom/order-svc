package com.supr.orderservice.enums;

public enum OrderItemEvent {
  CONFIRM,
  SLOTTED_CONFIRM,
  SLOTTED_CANCEL,
  APPROVE,
  ACCEPT,
  CANCEL,
  CANCEL_BY_SELLER,
  REPLACE_CANCELLED,
  PACK_AT_SELLER,
  SHIP_TO_CUSTOMER,
  DELIVER,
  REQUEST_CANCEL,
  REQUEST_REPLACEMENT,
  FAIL_PAYMENT,
  ASSIGNED_RIDER,
  CANCEL_BY_LOGISTICS,
  REPLACEMENT_APPROVED,
  RTO_INITIATED,
  RTO_CREATE,
  RVP_CREATE,
  RETURN_COMPLETE,
  PARTIAL_RETURN_COMPLETE,
  RETURN_REJECT,
  PARTIAL_RETURN_REJECT,
  FORCE_REFUND,
  PARTIAL_FORCE_REFUND,
  PAYMENT_CONFIRM,
  INITIATE_RETURN_UPDATE,
  RETURN_ACCEPT_COMPLETE,
  RETURN_REJECT_COMPLETE,
  DROPSHIP_PAYMENT_CONFIRM,
  CANCEL_DRAFT_BY_SELLER
}
