package com.supr.orderservice.service;


import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.request.PurchaseOrderRequest;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.model.response.PurchaseOrderResponse;

import javax.validation.Valid;

public interface PurchaseOrderService {
    PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request);

    PaymentProcessingResponse placeOrder(@Valid ProcessPaymentRequest processPaymentRequest);

}
