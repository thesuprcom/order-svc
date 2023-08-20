package com.supr.orderservice.repository;

import com.supr.orderservice.entity.UserSubscriptionStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSubscriptionStatusRepository extends JpaRepository<UserSubscriptionStatusEntity, Long> {
  Optional<UserSubscriptionStatusEntity> findByUserId(String userId);
}
