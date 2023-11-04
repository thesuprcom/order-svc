package com.supr.orderservice.model;

import com.supr.orderservice.enums.GiftSentOption;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserCartDTO {
    private List<ItemInfo> giftItems;
    private PriceDetails priceDetails;
    private BigDecimal amount;
    private String invitationLink;
    private GreetingCardMessage greetingCardMessage;
    private GreetingCard greetingCard;
    private UserInfo sender;
    private UserInfo receiver;
    private String userId;
    private Boolean autoPayEnabled;
    private SellerInfo sellerInfo;
    private Address billingAddress;
    private Address receiverAddress;
    private String countryCode;
    private String currencyCode;
    private List<UserInfo> receivers;
    private OrderSummaryDTO orderSummary;
    private List<BrandDisplayPrice> brandDisplayPrice;
    private GiftSentOption giftSentThrough;
    private CouponDetails couponDetails;
    private boolean isScheduled;
    private String scheduledDate;
    private String receiverEmail;
    private String receiverPhone;
}
