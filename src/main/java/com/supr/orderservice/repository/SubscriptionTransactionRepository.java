package com.supr.orderservice.repository;

import com.supr.orderservice.entity.SubscriptionTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionTransactionRepository extends JpaRepository<SubscriptionTransactionEntity, Long> {
  Optional<SubscriptionTransactionEntity> findFirstByPgOrderId(Long pgOrderId);
}
