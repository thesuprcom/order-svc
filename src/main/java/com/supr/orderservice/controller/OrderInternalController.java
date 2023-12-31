package com.supr.orderservice.controller;

import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.request.PurchaseOrderRequest;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.model.response.PurchaseOrderResponse;
import com.supr.orderservice.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/internal/sender")
@Slf4j
@RequiredArgsConstructor
public class OrderInternalController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/purchase")
    public PurchaseOrderResponse purchaseOrder(@Valid @RequestBody PurchaseOrderRequest purchaseOrderRequest) {
        return purchaseOrderService.purchaseOrder(purchaseOrderRequest);
    }

    @PostMapping("/place-order")
    public PaymentProcessingResponse senderPlaceOrder(@Valid @RequestBody ProcessPaymentRequest processPaymentRequest) {
        try {
            return purchaseOrderService.placeOrder(processPaymentRequest);
        } catch (Exception exception) {
            log.info("Exception occurred in place order.", exception);
            throw new OrderServiceException(ErrorEnum.USER_FRIENDLY_SOMETHING_WENT_WRONG);
        }
    }

}
