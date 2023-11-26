package com.supr.orderservice.repository;

import com.supr.orderservice.entity.CardDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface CardDetailsRepository extends JpaRepository<CardDetailsEntity, Long> {
    List<CardDetailsEntity> findByUserId(String userId);

    List<CardDetailsEntity> findTop5ByUserIdOrderByUpdatedAtDesc(String userId);

    Optional<CardDetailsEntity> findFirstByUserId(String userId);

    Optional<CardDetailsEntity> findByUserIdAndTokenId(String userId, String tokenId);

    Optional<CardDetailsEntity> findByUserIdAndSubscriptionIdNotNull(String userId);

    List<CardDetailsEntity> findBySubscriptionIdNotNullAndUpdatedAtBefore(Timestamp timestamp);

    @Modifying
    @Transactional
    void deleteByUserId(String userId);
}
