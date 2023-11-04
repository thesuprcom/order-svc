package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.enums.LogisticsCarrierStatus;
import com.supr.orderservice.enums.OrderItemType;
import com.supr.orderservice.model.CouponDetails;
import com.supr.orderservice.model.ImageUrl;
import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.Shipping;
import com.supr.orderservice.model.TrackingInfo;
import com.supr.orderservice.utils.ApplicationUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Audited
@Table(name = "order_items")
@Entity
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "hibernate_lazy_initializer", "handler"})
public class OrderItemEntity extends OrderBaseEntity{
    private String orderItemId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(updatable = false)
    private String countryCode;

    private String brandId;

    private String pskuCode;

    private String productId;

    private String parentProductId;

    private String warranty;
    @Enumerated(EnumType.STRING)
    private OrderItemType orderItemType;

    private String partnerSku;

    private String productTitle;
    private String productColour;
    private String productFullTitle;

    private String productBrand;

    private String productDescription;

    private String productFamily;

    private String productSubtype;

    private String productType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<ImageUrl> images;

    private String sellerId;

    private String parentSku;

    private String cancellationReason;

    private String replacementId;

    private LogisticsCarrierStatus logisticsCarrierStatus;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Shipping shipping;

    private BigDecimal orderItemQuantity;

    private BigDecimal totalPrice;

    private BigDecimal originalOrderItemQuantity;

    private BigDecimal merchantAcceptedQuantity;

    private BigDecimal returnUpdatableQuantity;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private OrderItemEntity referenceOrderItem;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private ItemInfo itemInfo;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private OrderPrice price;

    @Type(type = "json")
    @Column(name="item_tracking_info",columnDefinition = "json")
    private TrackingInfo itemTrackingInfo;

    private String updatedBy;


    @JsonIgnore
    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderItem")
    private List<OrderItemStatusHistoryEntity> orderItemStatusHistories = new LinkedList<>();

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private CouponDetails couponDetails;

    public void setOrderItemQuantity(BigDecimal orderItemQuantity) {
        if (orderItemQuantity != null) {
            this.orderItemQuantity = ApplicationUtils.roundUpToThreeDecimalPlaces(orderItemQuantity);
        }
    }

    public void setOriginalOrderItemQuantity(BigDecimal originalOrderItemQuantity) {
        if (originalOrderItemQuantity != null) {
            this.originalOrderItemQuantity = ApplicationUtils.roundUpToThreeDecimalPlaces(originalOrderItemQuantity);
        }
    }

    public void setMerchantAcceptedQuantity(BigDecimal merchantAcceptedQuantity) {
        if (merchantAcceptedQuantity != null) {
            this.merchantAcceptedQuantity = ApplicationUtils.roundUpToTwoDecimalPlaces(merchantAcceptedQuantity);
        }
    }

    public void setReturnUpdatableQuantity(BigDecimal returnUpdatableQuantity) {
        if (returnUpdatableQuantity != null) {
            this.returnUpdatableQuantity = ApplicationUtils.roundUpToTwoDecimalPlaces(returnUpdatableQuantity);
        }
    }
}
