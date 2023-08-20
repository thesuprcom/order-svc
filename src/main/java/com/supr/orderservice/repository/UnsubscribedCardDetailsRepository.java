package com.supr.orderservice.repository;

import com.supr.orderservice.entity.UnSubscribedCardDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnsubscribedCardDetailsRepository extends JpaRepository<UnSubscribedCardDetailsEntity, Long> {
}
