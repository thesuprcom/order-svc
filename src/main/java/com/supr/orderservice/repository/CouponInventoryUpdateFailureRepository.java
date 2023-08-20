package com.supr.orderservice.repository;

import com.supr.orderservice.entity.CouponInventoryUpdateFailureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponInventoryUpdateFailureRepository
    extends JpaRepository<CouponInventoryUpdateFailureEntity, Long> {
}
