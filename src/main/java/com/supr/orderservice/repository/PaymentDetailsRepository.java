package com.supr.orderservice.repository;

import com.supr.orderservice.entity.PaymentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetailsEntity, Long> {

  boolean existsByNnOrderIdAndPaymentOrderStatus(Long nnOrderId, String paymentOrderStatus);
}