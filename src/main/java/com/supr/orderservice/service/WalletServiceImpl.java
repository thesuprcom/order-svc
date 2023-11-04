package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.WalletOperation;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.UserDetails;
import com.supr.orderservice.model.UserInfo;
import com.supr.orderservice.model.request.WalletCreateRequest;
import com.supr.orderservice.model.request.WalletUpdateRequest;
import com.supr.orderservice.model.response.WalletResponse;
import com.supr.orderservice.service.external.WalletServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletServiceClient walletServiceClient;

    @Override
    public WalletResponse processRefund(UserInfo userDetails, BigDecimal refundAmount, OrderEntity orderEntity) {
        WalletResponse walletResponse;
        try {
            walletResponse = walletServiceClient.fetchWalletDetails(userDetails.getUserId());
            if (walletResponse == null) {
                log.info("Unable to fetch the wallet details for the userId: {}", userDetails.getUserId());
                WalletCreateRequest walletCreateRequest = new WalletCreateRequest();
                walletCreateRequest.setTotalAmount(refundAmount.toString());
                walletCreateRequest.setUserEmail(userDetails.getEmailId());
                walletCreateRequest.setOrderId(orderEntity.getOrderId());
                walletCreateRequest.setUserId(userDetails.getUserId());
                walletResponse = walletServiceClient.createWallet(walletCreateRequest, orderEntity.getCountryCode(),
                        orderEntity.getCurrencyCode());
            } else {
                log.error("Wallet details are present for the userId: {}", userDetails.getUserId());
                WalletUpdateRequest walletUpdateRequest = new WalletUpdateRequest();
                walletUpdateRequest.setWalletId(walletResponse.getWalletId());
                walletUpdateRequest.setWalletOperation(WalletOperation.INC);
                walletUpdateRequest.setAmount(refundAmount.toString());
                walletUpdateRequest.setOrderId(orderEntity.getOrderId());
                walletResponse = walletServiceClient.updateWallet(walletUpdateRequest, orderEntity.getCountryCode(),
                        orderEntity.getCurrencyCode());
            }
        } catch (Exception exception) {
            log.error("Error while fetching the Wallet details for the user: {}", userDetails.getUserId());
            throw new OrderServiceException("Unable to fetch the wallet details");
        }
        return walletResponse;
    }

    @Override
    public WalletResponse processRedeemtion(UserInfo userDetails, BigDecimal redeemAmount, OrderEntity orderEntity) {
        WalletResponse walletResponse;
        try {
            walletResponse = walletServiceClient.fetchWalletDetails(userDetails.getUserId());
            if (walletResponse == null) {
                log.info("Unable to fetch the wallet details for the userId: {}", userDetails.getUserId());
                throw new OrderServiceException("Unable to fetch the wallet details");
            } else {
                log.error("Wallet details are present for the userId: {}", userDetails.getUserId());
                WalletUpdateRequest walletUpdateRequest = new WalletUpdateRequest();
                walletUpdateRequest.setWalletId(walletResponse.getWalletId());
                walletUpdateRequest.setWalletOperation(WalletOperation.DEC);
                walletUpdateRequest.setAmount(redeemAmount.toString());
                walletUpdateRequest.setOrderId(orderEntity.getOrderId());
                walletResponse = walletServiceClient.updateWallet(walletUpdateRequest, orderEntity.getCountryCode(),
                        orderEntity.getCurrencyCode());
            }
        } catch (Exception exception) {
            log.error("Error while fetching the Wallet details for the user: {}", userDetails.getUserId());
            throw new OrderServiceException("Unable to fetch the wallet details");
        }
        return walletResponse;
    }
}
