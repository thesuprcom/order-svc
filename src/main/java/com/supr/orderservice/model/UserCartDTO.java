package com.supr.orderservice.model;

import lombok.Data;

import java.util.List;

@Data
public class UserCartDTO {
    private String storeId;
    private String brandId;
    private GreetingCard greetingCard;
    private List<ItemInfo> items;
    private OrderPrice priceDetails;
    private CouponDetails couponDetails;
    private Address shippingAddress;
    private UserDetails receiver;
}
