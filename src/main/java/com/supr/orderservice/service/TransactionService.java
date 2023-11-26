package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.TransactionEntity;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.response.PaymentProcessingResponse;

public interface TransactionService {

    TransactionEntity createTransaction(OrderEntity order);

    PaymentProcessingResponse processTransaction(OrderEntity order, ProcessPaymentRequest request);

    TransactionEntity processRefundPayment(OrderEntity order);

    TransactionEntity processPartialRefundPayment(OrderEntity order);

}
