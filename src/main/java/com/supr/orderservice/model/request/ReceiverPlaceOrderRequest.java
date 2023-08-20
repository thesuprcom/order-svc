package com.supr.orderservice.model.request;

import com.supr.orderservice.model.Address;
import lombok.Data;

@Data
public class ReceiverPlaceOrderRequest {
    private String orderId;
    private Address address;
    private String phoneNumber;
    private String emailId;

}
