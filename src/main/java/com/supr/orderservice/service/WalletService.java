package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.model.UserDetails;
import com.supr.orderservice.model.response.WalletResponse;

import java.math.BigDecimal;

public interface WalletService {
    WalletResponse processRefund(UserDetails userDetails, BigDecimal refundAmount, OrderEntity orderEntity);

    WalletResponse processRedeemtion(UserDetails userDetails, BigDecimal redeemAmount, OrderEntity orderEntity);
}
