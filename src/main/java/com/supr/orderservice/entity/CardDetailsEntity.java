package com.supr.orderservice.entity;

import com.google.common.collect.Sets;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.SubscriptionStatus;
import com.supr.orderservice.model.SubscriptionStatusDetails;
import com.supr.orderservice.utils.EncryptionUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Optional;

@Entity
@Builder
@Audited
@NoArgsConstructor
@Table(name = "card_details")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardDetailsEntity extends BaseEntity {
    @Version
    private Long version;

    @Getter
    @Setter
    @Column(name = "user_id")
    private String userId;

    @Getter
    @Setter
    @Column(name = "token_id")
    private String tokenId;

    @Getter
    @Setter
    @Column(name = "card_type")
    private String cardType;

    @Getter
    @Setter
    @Column(name = "brand")
    private String brand;

    @Getter
    @Setter
    @Column(name = "payment_info")
    private String paymentInfo;

    @Column(name = "expiry_month")
    @Getter
    private String expiryMonth;

    @Column(name = "expiry_year")
    @Getter
    private String expiryYear;

    @Getter
    @Setter
    @Column(name = "subscription_id")
    private String subscriptionId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Getter
    @Setter
    @Type(type = "json")
    @Column(name = "subscription_status_details")
    private SubscriptionStatusDetails subscriptionStatusDetails;

    public String getExpiryMonth(EncryptionUtil encryptionUtil) {
        return encryptionUtil.decrypt(this.expiryMonth);
    }

    public String getExpiryYear(EncryptionUtil encryptionUtil) {
        return encryptionUtil.decrypt(this.expiryYear);
    }

    public SubscriptionStatusDetails getSubscriptionStatusDetails() {
        return Optional.ofNullable(subscriptionStatusDetails)
                .orElse(new SubscriptionStatusDetails(SubscriptionStatus.UNLOCKED, Sets.newHashSet()));
    }

    public void unlockForNewSubscription() {
        final SubscriptionStatusDetails subscriptionStatusDetails = getSubscriptionStatusDetails();
        subscriptionStatusDetails.setSubscriptionStatus(SubscriptionStatus.UNLOCKED_FOR_NEW_SUBSCRIPTION);
        this.setSubscriptionStatusDetails(subscriptionStatusDetails);
    }

    public void addOrderIdToPaymentPendingOrder(String orderId) {
        SubscriptionStatusDetails subscriptionStatusDetails = getSubscriptionStatusDetails();
        subscriptionStatusDetails.getPaymentPendingOrder().add(orderId);
        subscriptionStatusDetails.setSubscriptionStatus(SubscriptionStatus.LOCKED);
        this.setSubscriptionStatusDetails(subscriptionStatusDetails);
    }

    public void removeOrderIdFromPaymentPendingOrder(String orderId) {
        SubscriptionStatusDetails subscriptionStatusDetails = getSubscriptionStatusDetails();
        subscriptionStatusDetails.getPaymentPendingOrder().remove(orderId);
        if (subscriptionStatusDetails.getPaymentPendingOrder().isEmpty()) {
            subscriptionStatusDetails.setSubscriptionStatus(SubscriptionStatus.UNLOCKED);
        }
        this.setSubscriptionStatusDetails(subscriptionStatusDetails);
    }
}

