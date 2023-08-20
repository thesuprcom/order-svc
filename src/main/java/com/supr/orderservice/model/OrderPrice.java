package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
public class OrderPrice {
    @JsonIgnore
    private final Map<String, Object> additionalProps = new HashMap<>();
    private BigDecimal totalMrp;
    private BigDecimal totalPrice;
    private BigDecimal totalOfferPrice;
    private BigDecimal totalDiscount;
    private BigDecimal totalShipping;
    private BigDecimal totalVat;
    private BigDecimal totalCommission;
    private BigDecimal totalBufferAmount;
    private BigDecimal totalCouponDiscount;
    private BigDecimal totalMerchantCouponDiscount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal refundItemAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal inputRefundItemAmount;

    public BigDecimal getTotalMrp() {
        return totalMrp.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
    }

    public void setTotalMrp(BigDecimal totalMrp) {
        if (totalMrp != null) {
            this.totalMrp = totalMrp.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
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

    public BigDecimal getTotalOfferPrice() {
        return totalOfferPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
    }

    public void setTotalOfferPrice(BigDecimal totalOfferPrice) {
        if (totalOfferPrice != null) {
            this.totalOfferPrice = totalOfferPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        if (totalDiscount != null) {
            this.totalDiscount = totalDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
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

    public BigDecimal getTotalVat() {
        return totalVat.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
    }

    public void setTotalVat(BigDecimal totalVat) {
        if (totalVat != null) {
            this.totalVat = totalVat.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getTotalCommission() {
        return totalCommission.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        if (totalCommission != null) {
            this.totalCommission = totalCommission.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getTotalBufferAmount() {
        return Optional.ofNullable(totalBufferAmount)
                .map(amount -> amount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE))
                .orElse(BigDecimal.ZERO);
    }

    public void setTotalBufferAmount(BigDecimal totalBufferAmount) {
        if (totalBufferAmount != null) {
            this.totalBufferAmount = totalBufferAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getTotalCouponDiscount() {
        return Optional.ofNullable(totalCouponDiscount)
                .map(amount -> amount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE))
                .orElse(BigDecimal.ZERO);
    }

    public void setTotalCouponDiscount(BigDecimal totalCouponDiscount) {
        if (totalCouponDiscount != null) {
            this.totalCouponDiscount = totalCouponDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getTotalMerchantCouponDiscount() {
        return Optional.ofNullable(totalMerchantCouponDiscount)
                .map(amount -> amount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE))
                .orElse(BigDecimal.ZERO);
    }

    public void setTotalMerchantCouponDiscount(BigDecimal totalMerchantCouponDiscount) {
        if (totalMerchantCouponDiscount != null) {
            this.totalMerchantCouponDiscount = totalMerchantCouponDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getRefundItemAmount() {
        if (refundItemAmount != null) {
            return refundItemAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
        return null;
    }

    public void setRefundItemAmount(BigDecimal refundItemAmount) {
        if (refundItemAmount != null) {
            this.refundItemAmount = refundItemAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    public BigDecimal getInputRefundItemAmount() {
        if (inputRefundItemAmount != null) {
            return inputRefundItemAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
        return null;
    }

    public void setInputRefundItemAmount(BigDecimal inputRefundItemAmount) {
        if (inputRefundItemAmount != null) {
            this.inputRefundItemAmount = inputRefundItemAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProps;
    }

    @JsonAnySetter
    public void setAdditionalProps(String key, Object value) {
        this.additionalProps.put(key, value);
    }

    public static final class OrderPriceBuilder {
        private BigDecimal totalMrp;
        private BigDecimal totalPrice;
        private BigDecimal totalOfferPrice;
        private BigDecimal totalDiscount;
        private BigDecimal totalShipping;
        private BigDecimal totalVat;
        private BigDecimal totalCommission;
        private BigDecimal totalBufferAmount;
        private BigDecimal totalCouponDiscount;
        private BigDecimal totalMerchantCouponDiscount;
        private BigDecimal refundItemAmount;

        private OrderPriceBuilder() {
        }

        public static OrderPriceBuilder builder() {
            return new OrderPriceBuilder();
        }

        public OrderPriceBuilder totalMrp(BigDecimal totalMrp) {
            this.totalMrp = totalMrp.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalOfferPrice(BigDecimal totalOfferPrice) {
            this.totalOfferPrice = totalOfferPrice.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
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

        public OrderPriceBuilder totalVat(BigDecimal totalVat) {
            this.totalVat = totalVat.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalCommission(BigDecimal totalCommission) {
            this.totalCommission = totalCommission.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalBufferAmount(BigDecimal totalBufferAmount) {
            this.totalBufferAmount = totalBufferAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalCouponDiscount(BigDecimal totalCouponDiscount) {
            this.totalCouponDiscount = totalCouponDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder totalMerchantCouponDiscount(BigDecimal totalMerchantCouponDiscount) {
            this.totalMerchantCouponDiscount = totalMerchantCouponDiscount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPriceBuilder refundItemAmount(BigDecimal refundItemAmount) {
            this.refundItemAmount = refundItemAmount.setScale(SCALE_FOR_PRICE, ROUNDING_MODE_FOR_PRICE);
            return this;
        }

        public OrderPrice build() {
            OrderPrice orderPrice = new OrderPrice();
            orderPrice.setTotalMrp(totalMrp);
            orderPrice.setTotalPrice(totalPrice);
            orderPrice.setTotalOfferPrice(totalOfferPrice);
            orderPrice.setTotalDiscount(totalDiscount);
            orderPrice.setTotalShipping(totalShipping);
            orderPrice.setTotalVat(totalVat);
            orderPrice.setTotalCommission(totalCommission);
            orderPrice.setTotalBufferAmount(totalBufferAmount);
            orderPrice.setTotalCouponDiscount(totalCouponDiscount);
            orderPrice.setTotalMerchantCouponDiscount(totalMerchantCouponDiscount);
            orderPrice.setRefundItemAmount(refundItemAmount);
            return orderPrice;
        }
    }
}
