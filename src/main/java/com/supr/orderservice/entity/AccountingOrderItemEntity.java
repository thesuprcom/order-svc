package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.enums.CouponIssuerType;
import com.supr.orderservice.enums.CouponType;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.utils.ApplicationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity(name = "accounting_order_item_details")
@Table(name = "accounting_order_item_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "hibernate_lazy_initializer", "handler"})
public class AccountingOrderItemEntity extends BaseEntity {

  @JoinColumn(name = "store_id")
  private String storeId;

  @JoinColumn(name = "order_id")
  private String orderId;

  @JoinColumn(name = "order_item_id")
  private String orderItemId;

  @Enumerated(EnumType.STRING)
  private OrderItemStatus status;

  @Type(type = "json")
  @Column(columnDefinition = "json")
  private OrderPrice price;

  @Column(name = "quantity")
  private BigDecimal quantity;

  @Enumerated(EnumType.STRING)
  @Column(name = "external_status")
  private ExternalStatus externalStatus;

  @Column(name = "invoice_number")
  private String invoiceNumber;

  @Column(name = "country")
  private String country;

  @Column(name = "city")
  private String city;

  @Column(name = "order_date")
  private Timestamp orderDate;

  @Column(name = "nn_order_id")
  private Long nnOrderId;

  @Column(name = "invoice_creation_date")
  private Timestamp invoiceCreationDate;

  @Column(name = "item_delivered_time")
  private Timestamp itemDeliveredTime;

  @Column(name = "cancellation_reason")
  private String cancellationReason;

  @Column(name = "bar_code")
  private String barCode;

  @Column(name = "sku")
  private String sku;

  @Column(name = "item_info")
  private String itemInfo;

  @Enumerated
  @Column(name = "coupon_issuer_type")
  private CouponIssuerType couponIssuerType;

  @Column(name = "delivery_invoice_number")
  private String deliveryInvoiceNumber;

  @Column(name = "delivery_invoice_creation_date")
  private Timestamp deliveryInvoiceCreationDate;

  @Column(name = "shipping_fee")
  private BigDecimal shippingFee;

  @Column(name = "coupon_type")
  @Enumerated(EnumType.STRING)
  private CouponType couponType;

  @Column(name = "delivery_pass_id")
  private String deliveryPassId;

  @Column(name = "delivery_type")
  private String deliveryType;

  public BigDecimal getQuantity() {
    return ApplicationUtils.roundUpToThreeDecimalPlaces(this.quantity);
  }
}
