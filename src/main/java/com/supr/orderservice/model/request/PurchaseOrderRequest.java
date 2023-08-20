package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.supr.orderservice.model.Address;
import com.supr.orderservice.model.SellerInfo;
import com.supr.orderservice.model.UserCartDTO;
import com.supr.orderservice.model.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderRequest {
    @JsonUnwrapped
    private UserCartDTO userCartDTO;
    private String orderId;
    private String brandId;
    private SellerInfo sellerInfo;
    private UserDetails senderDetails;
    private String giftSentOption;
    private String receiverEmail;
    private String receiverPhone;
    private String receiverInvitationLink;
    private String paymentMode;
    private String countryCode;
    private String currencyCode;
    private String ipAddress;
    private Address billingAddress;
    private boolean isScheduledOrder;
    private String scheduleDate;
}
