package com.supr.orderservice.service;

import com.supr.orderservice.model.request.GiftConfirmRequest;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.request.ReceiverPlaceOrderRequest;
import com.supr.orderservice.model.request.SwapGiftRequest;
import com.supr.orderservice.model.request.VerifyGiftOtpRequest;
import com.supr.orderservice.model.request.VerifyGiftRequest;
import com.supr.orderservice.model.response.AcceptGiftResponse;
import com.supr.orderservice.model.response.CustomerOrderResponse;
import com.supr.orderservice.model.response.FetchGreetingResponse;
import com.supr.orderservice.model.response.GiftConfirmResponse;
import com.supr.orderservice.model.response.GiftSwapOptionsResponse;
import com.supr.orderservice.model.response.ItemDetailResponse;
import com.supr.orderservice.model.response.OpenGreetingResponse;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.model.response.ReceiverPlaceOrderResponse;
import com.supr.orderservice.model.response.SwapGiftResponse;
import com.supr.orderservice.model.response.VerifyGiftResponse;
import com.supr.orderservice.model.response.ViewGiftResponse;

import java.util.List;

public interface ReceiverOrderService {
    FetchGreetingResponse fetchGreeting(String orderId);
    OpenGreetingResponse openGreeting(String orderId);
    ViewGiftResponse viewGift(String orderId);
    ItemDetailResponse fetchItemDetails(String orderId, String itemId);

    GiftSwapOptionsResponse fetchSwapOptions(String orderId);

    SwapGiftResponse swapGift(SwapGiftRequest swapGiftRequest);

    AcceptGiftResponse acceptGift(String orderId);

    List<CustomerOrderResponse> orderHistory(String userId);

    ReceiverPlaceOrderResponse receiverPlaceOrder(ReceiverPlaceOrderRequest placeOrderRequest);

    PaymentProcessingResponse receiverOrderPayment(ProcessPaymentRequest processPaymentRequest);

    VerifyGiftResponse verifyGift(VerifyGiftRequest verifyGiftRequest);

    VerifyGiftResponse verifyGiftOtp(VerifyGiftOtpRequest verifyGiftOtpRequest);

    GiftConfirmResponse confirmGift(String orderId, GiftConfirmRequest giftConfirmRequest);
}
