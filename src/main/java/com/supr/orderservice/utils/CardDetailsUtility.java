package com.supr.orderservice.utils;

import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.repository.CardDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardDetailsUtility {
  private final CardDetailsRepository cardDetailsRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Retryable(value = OptimisticLockingFailureException.class)
  public CardDetailsEntity addOrderIdToPaymentPendingOrder(final String userId, final String orderId) {
    return cardDetailsRepository.findFirstByUserId(userId)
        .map(cardDetailsEntity -> {
          cardDetailsEntity.addOrderIdToPaymentPendingOrder(orderId);
          return cardDetailsRepository.save(cardDetailsEntity);
        }).orElse(null);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Retryable(value = OptimisticLockingFailureException.class)
  public CardDetailsEntity removeOrderIdFromPaymentPendingOrder(final String userId, final String orderId) {
    final CardDetailsEntity cardDetailsEntity = getCardDetails(userId);
    cardDetailsEntity.removeOrderIdFromPaymentPendingOrder(orderId);
    return cardDetailsRepository.save(cardDetailsEntity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Retryable(value = OptimisticLockingFailureException.class)
  public void unlockForNewSubscription(final String userId) {
    CardDetailsEntity cardDetailsEntity = getCardDetails(userId);
    cardDetailsEntity.unlockForNewSubscription();
    cardDetailsRepository.save(cardDetailsEntity);
  }

  public CardDetailsEntity getCardDetails(String userId) {
    return cardDetailsRepository.findFirstByUserId(userId)
        .orElseThrow(() -> new OrderServiceException(ErrorEnum.NO_ACTIVE_SUBSCRIPTION, HttpStatus.BAD_REQUEST));
  }
}
