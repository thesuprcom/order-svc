package com.supr.orderservice.service.impl;

import com.google.common.collect.Lists;
import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.TransactionEntity;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.enums.NextAction;
import com.supr.orderservice.enums.PaymentActionEnum;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.PaymentReason;
import com.supr.orderservice.enums.PaymentStatus;
import com.supr.orderservice.enums.TransactionStatus;
import com.supr.orderservice.enums.TransactionType;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.exception.PaymentProcessingException;
import com.supr.orderservice.model.PaymentProcessingResult;
import com.supr.orderservice.model.pg.response.MamoPayPaymentLinkResponse;
import com.supr.orderservice.model.request.PaymentRequest;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.model.response.PaymentOrderResponse;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.repository.CardDetailsRepository;
import com.supr.orderservice.repository.OrderRepository;
import com.supr.orderservice.service.CloudTaskEnqueueService;
import com.supr.orderservice.service.PaymentGatewayService;
import com.supr.orderservice.service.TransactionService;
import com.supr.orderservice.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final OrderRepository orderRepository;
    private final PaymentGatewayService pgService;
    private final CardDetailsRepository cardDetailsRepository;
    private final CloudTaskEnqueueService cloudTaskEnqueueService;

    @Override
    public TransactionEntity createTransaction(OrderEntity order) {
        TransactionEntity transactionVO = Optional.ofNullable(order.getTransaction()).orElseGet(TransactionEntity::new);
        transactionVO.setAmount(order.getTotalAmount());
        BigDecimal amountPayable = getAmountPayable(order);
        transactionVO.setCurrency(order.getCurrencyCode());
        transactionVO.setAmountPayable(amountPayable);
        transactionVO.setOrder(order);
        transactionVO.setStatus(TransactionStatus.CREATED);
        transactionVO.setType(TransactionType.DEBIT);
        transactionVO.setTransactionId(Optional.ofNullable(order.getTransaction()).map(TransactionEntity::getTransactionId)
                .orElseGet(ApplicationUtils::generateTransactionId));
        return transactionVO;
    }

    @Override
    public PaymentProcessingResponse processTransaction(OrderEntity order, ProcessPaymentRequest request) {
        PaymentMode paymentMode = request.getPaymentModeSelected();
        TransactionEntity transaction = order.getTransaction();
        transaction.setPaymentModeSelected(paymentMode);

        switch (paymentMode) {
            case Card:
                if (request.getCardData() != null && request.getCardData().getCardId() != null) {
                    transaction.setCardId(request.getCardData().getCardId());
                    order.setTransaction(transaction);
                    transaction = processPayment(order, PaymentActionEnum.CREATE_SAVED_CARD_PAYMENT_LINK);
                } else {
                    transaction = processPayment(order, PaymentActionEnum.CREATE_PAYMENT_LINK);
                }
                order.setTransaction(transaction);
                orderRepository.save(order);
                return buildPaymentProcessingResponse(order.getOrderId(), transaction);
        }
        throw createOrderServiceException(ErrorEnum.INVALID_PAYMENT_MODE);
    }

    @Override
    public TransactionEntity processRefundPayment(OrderEntity order) {
        return processPayment(order, PaymentActionEnum.REFUND);
    }

    @Override
    public TransactionEntity processPartialRefundPayment(OrderEntity order) {
        return processPayment(order, PaymentActionEnum.PARTIAL_REFUND);
    }

    private TransactionEntity processPayment(OrderEntity order, PaymentActionEnum paymentActionEnum) {
        TransactionEntity transaction = order.getTransaction();
        transaction.setStatus(paymentActionEnum.getSuccessTransactionStatus());
        transaction.setType(paymentActionEnum.getTransactionType());

        if (transaction.getPaymentModeSelected() == PaymentMode.CashOnDelivery) {
            return transaction;
        }

        Optional<PaymentGatewayResponse> responseOptional = callPaymentGateway(order, paymentActionEnum);

        if (responseOptional.isPresent() && responseOptional.get().getResponse() != null) {
            PaymentGatewayResponse response = responseOptional.get();
            log.info("Payment gateway response: {}", response);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setPaymentGatewayResponse(response);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }

    private OrderServiceException createOrderServiceException(final ErrorEnum noActiveSubscription) {
        return new OrderServiceException(noActiveSubscription, HttpStatus.BAD_REQUEST);
    }

    private void validateExistingSubscription(final OrderEntity order) {
        String userId = order.getUserId();
        final CardDetailsEntity cardDetailsEntity = cardDetailsRepository.findFirstByUserId(userId)
                .orElseThrow(() -> createOrderServiceException(ErrorEnum.NO_ACTIVE_SUBSCRIPTION));

        if (cardDetailsEntity.getSubscriptionId() == null) {
            throw createOrderServiceException(ErrorEnum.NO_ACTIVE_SUBSCRIPTION);
        }

    }

    private PaymentProcessingResponse buildPaymentProcessingResponse(final String orderId, TransactionEntity transaction) {
        if (transaction.getPgOrderStatus() == TransactionStatus.FAILED_TO_CREATE_PAYMENT_LINK) {
            return PaymentProcessingResponse.builder()
                    .orderId(orderId)
                    .status("FAILED")
                    .returnUrl(null)
                    .paymentProcessingResult(buildPaymentProcessingResult(PaymentStatus.FAILED))
                    .build();
        }
        MamoPayPaymentLinkResponse paymentLinkResponse = transaction.getPaymentGatewayResponse().getResponse();
        return PaymentProcessingResponse.builder()
                .orderId(orderId)
                .returnUrl(paymentLinkResponse.getPaymentUrl())
                .status("SUCCESS")
                .paymentProcessingResult(buildPaymentProcessingResult(PaymentStatus.INITIATED))
                .build();
    }

    private PaymentProcessingResult buildPaymentProcessingResult(final PaymentStatus paymentStatus) {
        String msg = null;
        if (paymentStatus == PaymentStatus.INITIATED) {
            msg = " Payment initiated";
        }
        return PaymentProcessingResult.builder().paymentStatus(paymentStatus).message(msg).build();
    }

    private BigDecimal getAmountPayable(OrderEntity order) {
        return order.getTotalAmount().max(BigDecimal.ZERO);
    }

    public PaymentOrderResponse getPaymentServiceResponse(OrderEntity order) {
        final PaymentGatewayResponse paymentGatewayResponse = pgService.enquireGateway(order);
        return null;
    }

    public void enqueueAuthorizePaymentRequest(final OrderEntity order, NextAction action) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getOrderId())
                .paymentReason(PaymentReason.USER_ORDER)
                .userId(order.getUserId())
                .nextActions(Lists.newArrayList(NextAction.CAPTURE_PAYMENT_FOR_USER_ORDER))
                .build();

        cloudTaskEnqueueService.enqueueNextAction(action, paymentRequest);
    }

    private void processPaymentRefund(OrderEntity order) {
        processRefundPayment(order);
    }

    private void processPaymentPartialRefund(OrderEntity order, Optional<PaymentActionEnum> paymentActionEnumOptional,
                                             PaymentOrderResponse noonOrderResponseDTO) {
        if (!validateAndCheckIfRefundAlreadyProcessed(order, paymentActionEnumOptional.get(), noonOrderResponseDTO)) {
            processPartialRefundPayment(order);
        }
    }

    private void processPaymentRefund(OrderEntity order, Optional<PaymentActionEnum> paymentActionEnumOptional,
                                      PaymentOrderResponse noonOrderResponseDTO) {
        if (!validateAndCheckIfRefundAlreadyProcessed(order, paymentActionEnumOptional.get(), noonOrderResponseDTO)) {
            processRefundPayment(order);
        }
    }

    private boolean validateAndCheckIfRefundAlreadyProcessed(OrderEntity order, PaymentActionEnum paymentActionEnum,
                                                             PaymentOrderResponse noonOrderResponseDTO) {
        BigDecimal refundAmount =
                Optional.ofNullable(order.getPrice().getRefundItemAmount()).orElseGet(() -> order.getPrice().getTotalPrice());
        if (paymentActionEnum.getResponseOrderStatus().equalsIgnoreCase(noonOrderResponseDTO.getStatus())) {
            if (noonOrderResponseDTO.getTotalRefundedAmount().compareTo(refundAmount) != 0) {
                throw new PaymentProcessingException(ErrorEnum.MISMATCH_IN_REFUND_AMOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("Refund is already triggered for this order {}. Hence moving on", order.getOrderId());
            return true;
        }
        return false;
    }

    private Optional<PaymentGatewayResponse> callPaymentGateway(OrderEntity order, PaymentActionEnum paymentActionEnum) {
        switch (paymentActionEnum) {
            case CREATE_PAYMENT_LINK:
                return Optional.of(pgService.createPaymentLink(order));
            case CREATE_SAVED_CARD_PAYMENT_LINK:
                return Optional.of(pgService.createSavedCardPaymentLink(order));
            case REVERSE:
                return Optional.of(pgService.reversePayment(order));

            case CAPTURE:
                return Optional.of(pgService.capturePayment(order));

            case REFUND:
                return Optional.of(pgService.refundPayment(order));

            case PARTIAL_REFUND:
                return Optional.of(pgService.partialRefundPayment(order));

            default:
                return Optional.empty();
        }
    }

}
