package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentActionEnum {
    AUTHORIZE("AUTHORIZE", "AUTHORIZED", TransactionStatus.AUTHORIZED, TransactionStatus.FAILED_TO_AUTHORIZE,
            TransactionType.DEBIT),
    CREATE_PAYMENT_LINK("CREATE_PAYMENT_LINK", "CREATE_PAYMENT_LINK", TransactionStatus.CREATE_PAYMENT_LINK, TransactionStatus.FAILED_TO_CREATE_PAYMENT_LINK,
            TransactionType.DEBIT),
    CREATE_SAVED_CARD_PAYMENT_LINK("CREATE_SAVED_CARD_PAYMENT_LINK", "CREATE_SAVED_CARD_PAYMENT_LINK", TransactionStatus.CREATE_SAVED_CARD_PAYMENT_LINK,
            TransactionStatus.FAILED_TO_CREATE_SAVED_CARD_PAYMENT_LINK,TransactionType.DEBIT),
    CAPTURE("CAPTURE", "CAPTURED", TransactionStatus.SUCCESSFUL, TransactionStatus.FAILED_TO_CAPTURE,
            TransactionType.DEBIT),
    REVERSE("REVERSE", "REVERSED", TransactionStatus.REVERSED, TransactionStatus.FAILED_TO_REVERSE,
            TransactionType.CREDIT),
    REFUND("REFUND", "REFUNDED", TransactionStatus.REFUNDED, TransactionStatus.FAILED_TO_REFUND, TransactionType.CREDIT),
    PARTIAL_REFUND("REFUND", "PARTIALLY_REFUNDED", TransactionStatus.PARTIALLY_REFUNDED,
            TransactionStatus.FAILED_TO_PARTIALLY_REFUND, TransactionType.CREDIT);

    private String requestOperationName;
    private String responseOrderStatus;
    private TransactionStatus successTransactionStatus;
    private TransactionStatus failedTransactionStatus;
    private TransactionType transactionType;
}
