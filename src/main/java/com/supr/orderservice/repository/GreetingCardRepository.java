package com.supr.orderservice.repository;

import com.supr.orderservice.entity.GreetingCardEntity;
import com.supr.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GreetingCardRepository extends JpaRepository<GreetingCardEntity, Long> {
    Optional<GreetingCardEntity> findByOrderId(Long orderId);
}
