package com.supr.orderservice.utils;

import com.google.common.collect.ImmutableList;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.model.UserDetails;
import com.supr.orderservice.model.UserInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.supr.orderservice.enums.OrderItemStatus.CANCELLED;
import static com.supr.orderservice.enums.OrderItemStatus.CANCELLED_BY_SELLER;
import static com.supr.orderservice.enums.OrderItemStatus.DELIVERED;
import static com.supr.orderservice.enums.OrderItemStatus.FAILED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUtils {
    private static final Random random = new Random();
    private static final OrderItemStatus[] completedInternalStatus =
            new OrderItemStatus[]{CANCELLED, CANCELLED_BY_SELLER, DELIVERED, FAILED};
    private static final OrderItemStatus[] accountingStatus =
            new OrderItemStatus[]{CANCELLED, CANCELLED_BY_SELLER, DELIVERED};

    public static List<ExternalStatus> getSellerPortalExternalStatus() {
        return Arrays
                .asList(ExternalStatus.GIFT_CREATED, ExternalStatus.PROCESSING_ON_HOLD, ExternalStatus.SHIPPED,
                        ExternalStatus.DELIVERED, ExternalStatus.CANCELLED, ExternalStatus.PARTIALLY_DELIVERED,
                        ExternalStatus.PAYMENT_FAILED, ExternalStatus.GIFT_SCHEDULED, ExternalStatus.GIFT_PLACED,
                        ExternalStatus.UNDELIVERED, ExternalStatus.PARTIALLY_SHIPPED, ExternalStatus.PAYMENT_INITIATED,
                        ExternalStatus.GIFT_SWAPPED,ExternalStatus.GIFT_OPENED,ExternalStatus.GIFT_UNDELIVERED,
                        ExternalStatus.CANCELLED_BY_SELLER, ExternalStatus.CANCELLED);
    }

    public static List<ExternalStatus> getOrderCancellationExternalStatus() {
        return Arrays
                .asList(ExternalStatus.GIFT_CREATED, ExternalStatus.PROCESSING_ON_HOLD,
                        ExternalStatus.CANCELLED, ExternalStatus.GIFT_ACCEPTED);
    }

    public static Set<ExternalStatus> getSellerPortalOrderCancellationStatus() {
        return new HashSet(Arrays
                .asList(ExternalStatus.GIFT_CREATED, ExternalStatus.GIFT_ACCEPTED,
                        ExternalStatus.PARTIALLY_SHIPPED, ExternalStatus.PARTIALLY_DELIVERED,
                        ExternalStatus.PARTIALLY_CANCELLED));
    }

    public static List<ExternalStatus> getUserOrderHistoryExternalStatus() {
        return Arrays.asList(ExternalStatus.GIFT_CREATED, ExternalStatus.GIFT_SWAPPED);
    }

    public static BigDecimal roundUpToThreeDecimalPlaces(BigDecimal value) {
        return value.setScale(3, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundUpToTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static String generateOrderId() {
        return String.join("", "SUPR", Long.toString(System.currentTimeMillis(), 36),
                Integer.toString(random.nextInt(1260) + 36, 36)).toUpperCase();
    }

    public static String generateCardId() {
        return String.join("", "CARD", Long.toString(System.currentTimeMillis(), 36),
                Integer.toString(random.nextInt(1260) + 36, 36)).toUpperCase();
    }

    public static String generateTransactionId() {
        return String.join("", "TX", Long.toString(System.currentTimeMillis(), 36),
                Integer.toString(random.nextInt(1260) + 36, 36)).toUpperCase();
    }

    private static final Set<OrderItemStatus> completedInternalStatuses =
            new HashSet<>(Arrays.asList(completedInternalStatus));

    public static boolean isInternalStatusCompleted(OrderItemStatus orderItemStatus) {
        return completedInternalStatuses.contains(orderItemStatus);
    }

    private static final Set<OrderItemStatus> accountingStatuses =
            new HashSet<>(Arrays.asList(accountingStatus));

    public static boolean isAccountingStatus(OrderItemStatus orderItemStatus) {
        return accountingStatuses.contains(orderItemStatus);
    }

    public static String fetchFullName(UserInfo userDetail) {
        return Strings.join(Arrays.asList(userDetail.getFirstName(), userDetail.getLastName()), ' ');
    }
}
