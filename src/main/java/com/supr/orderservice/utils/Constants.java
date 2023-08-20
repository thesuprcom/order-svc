package com.supr.orderservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final int SCALE_FOR_PRICE = 2;
    public static final RoundingMode ROUNDING_MODE_FOR_PRICE = RoundingMode.HALF_UP;
    public static final String DELIVERY_INVOICE_PREFIX = "PI9113-AE-NNDC";
    public static final String NUMBER_OF_DIGITS_FOR_DELIVERY_FEES_INVOICE = "00000000";
    public static final String YEAR_IN_TWO_DIGITS = "YY";
    public static final String MONTH_IN_TWO_DIGITS = "MM";
    public static final String DATE_OBJECT_TIMESTAMP_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String COUPON_DISCOUNT_IS_NOT_APPLICABLE_ON_THIS_PAYMENT_METHOD =
            "Coupon discount is not applicable on this payment method!";
    public static final String ORDER_SERVICE = "order-service";
    public static final String STORE_ID_HEADER = "x-store-id";
    public static final String WRONG_PRICE_REASON = "Wrong price updated for items";
    public static final String X_COUNTRY_CODE_HEADER_KEY = "X-Country-Code";
    public static final String ORDER_SERVICE_EXTERNAL_API_PREFIX = "/order-service/external";
    public static final String PLACE_ORDER_TASK_NAME_PREFIX = "PLACE_ORDER_";
    public static final String SCHEDULE_AFTER_ZERO_SECOND = "PT0S";
    public static final String SCHEDULE_AFTER_ONE_SECOND = "PT1S";
    public static final String SCHEDULE_AFTER_FIVE_SECOND = "PT5S";
    public static final String SCHEDULE_AFTER_TEN_SECONDS = "PT10S";

    public static final String AUTHORIZE_PAYMENT_TASK_NAME_PREFIX = "AUTHORIZE_PAYMENT_";
    public static final String RETRY_AUTHORIZE_PAYMENT_TASK_NAME_PREFIX = "RETRY_AUTHORIZE_PAYMENT_";
    public static final String CANCEL_USER_ORDER_TASK_NAME_PREFIX = "CANCEL_ORDER_";
    public static final String SCHEDULED_CANCEL_USER_ORDER_TASK_NAME_PREFIX = "SCHEDULED_CANCEL_ORDER_";
    public static final String CREATE_SHIPMENT_TASK_NAME_PREFIX = "CREATE_SHIPMENT_";
    public static final String CAPTURE_PAYMENT_TASK_NAME_PREFIX = "CAPTURE_PAYMENT_";
    public static final String REFUND_PAYMENT_TASK_NAME_PREFIX = "REFUND_PAYMENT_";
    public static final String INTERNAL_ACCESS_TOKEN_HEADER = "X-Access-Token-Internal";
    public static final String SIGNATURE_HEADER = "X-Signature-Token";
    public static final String PAYMENT_FAILED_ORDER_STATUS = "FAILED";
    public static final String PAYMENT_PENDING_ORDER_STATUS = "PENDING";
    public static final String USER_ID_ATTRIBUTE = "userId";
    public static final String INVALID_USER = "Please re-login to authenticate yourself!";
    public static final String X_PAGE_CONTEXT = "X-Page-Context";
    public static final String GIFT_SWAPPED = "GIFT_SWAPPED";
    public static final String ACCEPTED_GIFT_WITHOUT_SWAP = "ACCEPTED_GIFT_WITHOUT_SWAP";
    public static final String ACCEPTED_GIFT_SWAP = "ACCEPTED_GIFT_SWAP";
}
