package com.supr.orderservice.controller;

import com.supr.orderservice.model.request.GiftConfirmRequest;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.request.ReceiverPlaceOrderRequest;
import com.supr.orderservice.model.request.SwapGiftRequest;
import com.supr.orderservice.model.request.VerifyGiftOtpRequest;
import com.supr.orderservice.model.request.VerifyGiftRequest;
import com.supr.orderservice.service.ReceiverOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/external/receiver")
@RequiredArgsConstructor
public class ReceiverOrderController {
    private final ReceiverOrderService receiverOrderService;

    @PostMapping("/verify/gift")
    public ResponseEntity verifyGift(@RequestBody @Valid VerifyGiftRequest verifyGiftRequest) {
        return new ResponseEntity(receiverOrderService.verifyGift(verifyGiftRequest), HttpStatus.OK);
    }

    @PostMapping("/verify/gift-otp")
    public ResponseEntity verifyGiftOtp(@RequestBody @Valid VerifyGiftOtpRequest verifyGiftOtpRequest) {
        return new ResponseEntity(receiverOrderService.verifyGiftOtp(verifyGiftOtpRequest), HttpStatus.OK);
    }

    @GetMapping("/fetch/greeting/{order-id}")
    public ResponseEntity fetchGreeting(@PathVariable(name = "order-id") String orderId) {
        return new ResponseEntity(receiverOrderService.fetchGreeting(orderId), HttpStatus.OK);
    }

    @GetMapping("/open/greeting/{order-id}")
    public ResponseEntity openGreeting(@PathVariable(name = "order-id") String orderId) {
        return new ResponseEntity(receiverOrderService.openGreeting(orderId), HttpStatus.OK);
    }

    @GetMapping("/open/gift/{order-id}")
    public ResponseEntity viewGift(@PathVariable(name = "order-id") String orderId) {
        return new ResponseEntity(receiverOrderService.viewGift(orderId), HttpStatus.OK);
    }

    @GetMapping("/gift/item-detail/{order-id}/{item-id}/{seller-id}")
    public ResponseEntity fetchItemDetails(@PathVariable(name = "order-id") String orderId,
                                           @PathVariable(name = "item-id") String itemId,
                                           @PathVariable(name = "seller-id") String sellerId) {
        return new ResponseEntity(receiverOrderService.fetchItemDetails(orderId, itemId, sellerId), HttpStatus.OK);
    }

    @GetMapping("/swap/item/{order-id}/{sku}")
    public ResponseEntity fetchSwapOptions(@PathVariable(name = "order-id") String orderId,
                                           @PathVariable(name = "sku") String sku) {
        return new ResponseEntity(receiverOrderService.fetchSwapOptions(orderId, sku), HttpStatus.OK);
    }

    @PostMapping("/swap/gift")
    public ResponseEntity swapGift(@RequestBody @Valid SwapGiftRequest swapGiftRequest) {
        return new ResponseEntity(receiverOrderService.swapGift(swapGiftRequest), HttpStatus.OK);
    }

    @PostMapping("/accept/gift/{order-id}")
    public ResponseEntity acceptGift(@PathVariable(name = "order-id") String orderId) {
        return new ResponseEntity(receiverOrderService.acceptGift(orderId), HttpStatus.OK);
    }

    @PostMapping("/confirm/gift/{order-id}")
    public ResponseEntity confirmGift(@PathVariable(name = "order-id") String orderId,
                                      @RequestBody GiftConfirmRequest giftConfirmRequest) {
        return new ResponseEntity(receiverOrderService.confirmGift(orderId, giftConfirmRequest), HttpStatus.OK);
    }

    @PostMapping("/receiver/place-order")
    public ResponseEntity receiverPlaceOrder(@RequestBody @Valid ReceiverPlaceOrderRequest placeOrderRequest) {
        return new ResponseEntity(receiverOrderService.receiverPlaceOrder(placeOrderRequest), HttpStatus.OK);
    }

    @PostMapping("/receiver/make-order-payment")
    public ResponseEntity receiverOrderPayment(@RequestBody @Valid ProcessPaymentRequest processPaymentRequest) {
        return new ResponseEntity(receiverOrderService.receiverOrderPayment(processPaymentRequest), HttpStatus.OK);
    }

    @GetMapping("/history/{user-id}")
    public ResponseEntity orderHistory(@PathVariable(name = "user-id") String userId) {
        return new ResponseEntity(receiverOrderService.orderHistory(userId), HttpStatus.OK);
    }
}
