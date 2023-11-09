package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.supr.orderservice.utils.Constants.ROUNDING_MODE_FOR_PRICE;
import static com.supr.orderservice.utils.Constants.SCALE_FOR_PRICE;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
public class OrderPrice {
    BigDecimal totalPrice;
    BigDecimal totalWalletPrice;
    BigDecimal finalPrice;
    BigDecimal totalTax;
    BigDecimal totalDiscount;
    BigDecimal totalCouponDiscount;
    BigDecimal totalShipping;
    BigDecimal refundItemAmount;

    public void setFinalPrice(BigDecimal finalPrice) {
        if (finalPrice != null) {
            this.finalPrice = finalPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public void setRefundItemAmount(BigDecimal refundItemAmount) {
        if (refundItemAmount != null) {
            this.refundItemAmount = refundItemAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public void setTotalTax(BigDecimal totalTax) {
        if (totalTax != null) {
            this.totalTax = totalTax.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        if (totalDiscount != null) {
            this.totalDiscount = totalDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public void setTotalCouponDiscount(BigDecimal totalCouponDiscount) {
        this.totalCouponDiscount = totalCouponDiscount;
    }

    public void setTotalWalletPrice(BigDecimal totalWalletPrice) {
        if (totalWalletPrice != null) {
            this.totalWalletPrice = totalWalletPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getTotalPrice() {
        return totalPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        if (totalPrice != null) {
            this.totalPrice = totalPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getTotalShipping() {
        return totalShipping.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
    }

    public void setTotalShipping(BigDecimal totalShipping) {
        if (totalShipping != null) {
            this.totalShipping = totalShipping.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public static final class OrderPriceBuilder {
        BigDecimal totalPrice;
        BigDecimal totalWalletPrice;
        BigDecimal finalPrice;
        BigDecimal totalTax;
        BigDecimal totalDiscount;
        BigDecimal totalCouponDiscount;
        BigDecimal totalShipping;
        BigDecimal refundItemAmount;
        private OrderPriceBuilder() {
        }

        public static OrderPriceBuilder builder() {
            return new OrderPriceBuilder();
        }

        public OrderPriceBuilder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder refundItemAmount(BigDecimal refundItemAmount) {
            this.refundItemAmount = refundItemAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalWalletPrice(BigDecimal totalWalletPrice) {
            this.totalWalletPrice = totalWalletPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder finalPrice(BigDecimal finalPrice) {
            this.finalPrice = finalPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalDiscount(BigDecimal totalDiscount) {
            this.totalDiscount = totalDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalShipping(BigDecimal totalShipping) {
            this.totalShipping = totalShipping.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalTax(BigDecimal totalTax) {
            this.totalTax = totalTax.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalCouponDiscount(BigDecimal totalCouponDiscount) {
            this.totalCouponDiscount = totalCouponDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPrice build() {
            OrderPrice orderPrice = new OrderPrice();
            orderPrice.setTotalPrice(totalPrice);
            orderPrice.setFinalPrice(finalPrice);
            orderPrice.setTotalWalletPrice(totalWalletPrice);
            orderPrice.setTotalDiscount(totalDiscount);
            orderPrice.setTotalShipping(totalShipping);
            orderPrice.setTotalTax(totalTax);
            orderPrice.setTotalCouponDiscount(totalCouponDiscount);
            orderPrice.setRefundItemAmount(refundItemAmount);
            return orderPrice;
        }
    }
}
