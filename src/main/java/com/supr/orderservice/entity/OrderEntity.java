package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.supr.orderservice.enums.GiftSentOption;
import com.supr.orderservice.enums.LogisticsCarrierStatus;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.model.Address;
import com.supr.orderservice.model.CouponDetails;
import com.supr.orderservice.model.DeliveryFeeInfo;
import com.supr.orderservice.model.GreetingCardMessage;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.Shipping;
import com.supr.orderservice.model.TrackingInfo;
import com.supr.orderservice.model.UserInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
public class OrderEntity extends OrderBaseEntity {
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
    private String cartIdentifier;
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
    private String notes;
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
    @EqualsAndHashCode.Exclude
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
    @NotAudited
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private CouponDetails couponDetails;
    @Audited
    private Timestamp orderPlacedTime;
    @Column(name = "package_count")
    private Integer packageCount;
    private String updatedBy;
    @Transient
    private DeliveryFeeInfo deliveryFeeInfo;
    private BigDecimal walletAmount;
    @JsonProperty("is_wallet_applied")
    private boolean isWalletApplied;

}
