package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supr.orderservice.enums.GiftSentOption;
import com.supr.orderservice.enums.LogisticsCarrierStatus;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.model.Address;
import com.supr.orderservice.model.CouponDetails;
import com.supr.orderservice.model.DeliveryFeeInfo;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.SellerInfo;
import com.supr.orderservice.model.Shipping;
import com.supr.orderservice.model.TrackingInfo;
import com.supr.orderservice.model.UserDetails;
import com.supr.orderservice.model.UserInfo;
import com.supr.orderservice.utils.CardDetailsUtility;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

@Entity
@Data
@Audited
@Builder
@NoArgsConstructor
@Table(name = "user_orders")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderEntity extends OrderBaseEntity {
    private String sellerId;
    private String brandId;
    @NaturalId
    private String orderId;
    @Column(updatable = false)
    private String userId;
    private String receiverUserId;
    @Column(updatable = false)
    private String currencyCode;
    @Column(updatable = false)
    private String countryCode;
    private BigDecimal totalAmount;
    private BigDecimal receiverTotalAmount;
    private String pinCode;
    @NotAudited
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private TransactionEntity transaction;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private OrderPrice price;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private OrderPrice receiverOrderPrice;
    private boolean isGiftSwapped;
    private String trackingId;
    private String invoiceDocumentId;
    private String invitationLink;
    private String receiverEmail;
    private String receiverPhone;
    private boolean isOrderScheduled;
    private Timestamp scheduledDate;
    private LogisticsCarrierStatus logisticsCarrierStatus;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Shipping shipping;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private UserInfo sender;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private UserInfo receiver;
    @Type(type = "json")
    @Column(name = "shipping_address", columnDefinition = "json")
    private Address shippingAddress;
    @Type(type = "json")
    @Column(name = "billing_address", columnDefinition = "json")
    private Address billingAddress;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private SellerInfo sellerInfo;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;
    @Type(type = "json")
    @Column(name = "order_tracking_info", columnDefinition = "json")
    private TrackingInfo orderTrackingInfo;
    private boolean isOrderLevelTracking;
    private String ipAddress;
    private PaymentMode paymentMode;
    @Enumerated(EnumType.STRING)
    @Column(name = "gift_sent_option")
    private GiftSentOption giftSentOption;
    @Column(name = "delivery_type")
    private String deliveryType;
    @NotAudited
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItemEntity> orderItemEntities = new LinkedList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "greeting_card_id")
    private GreetingCardEntity greetingCard;
    @NotAudited
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderStatusHistoryEntity> orderItemStatusHistories = new LinkedList<>();
    @NotAudited
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = {PERSIST, MERGE, REFRESH, DETACH})
    private OrderEntity referenceOrder;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private CouponDetails couponDetails;
    @Audited
    private Timestamp orderPlacedTime;
    @Column(name = "subscription_pending")
    private boolean subscriptionPending;
    @Column(name = "payment_authorization_failed_count")
    private int paymentAuthorizationFailedCount;
    @Setter(AccessLevel.PRIVATE)
    @Column(name = "payment_authorization_failed_timestamp")
    private Timestamp paymentAuthorizationFailedTimestamp;
    @Column(name = "package_count")
    private Integer packageCount;
    private String updatedBy;
    @Transient
    private DeliveryFeeInfo deliveryFeeInfo;

    private void setPaymentAuthorizationFailedCount(int count) {
        this.paymentAuthorizationFailedCount = count;

        if (count > 0) {
            this.setPaymentAuthorizationFailedTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
        }
    }

    public void decrementPaymentAuthorizationFailedCount(final CardDetailsUtility cardDetailsUtility) {
        this.setPaymentAuthorizationFailedCount(Math.max(0, this.getPaymentAuthorizationFailedCount() - 1));
        cardDetailsUtility.removeOrderIdFromPaymentPendingOrder(this.userId, this.orderId);
    }

    public void incrementPaymentAuthorizationFailedCount(final CardDetailsUtility cardDetailsUtility) {
        this.setPaymentAuthorizationFailedCount(this.getPaymentAuthorizationFailedCount() + 1);
        cardDetailsUtility.unlockForNewSubscription(userId);
    }

    public boolean isAuthorizationFailedForOrder() {
        return this.getPaymentAuthorizationFailedCount() > 0;
    }
}
