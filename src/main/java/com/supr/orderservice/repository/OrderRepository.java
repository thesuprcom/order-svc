package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {


    @Query("SELECT o.externalStatus, COUNT(o) FROM OrderEntity o " +
            "WHERE o.externalStatus IN :statusList " +
            "AND o.countryCode = :countryCode " +
            "AND o.createdAt >= :startDate " +
            "GROUP BY o.status")
    List<Object[]> getOrderCountByStatusAndCountryCodeAndSellerIdAndDateGroupByStatus(
            @Param("statusList") List<ExternalStatus> statusList,
            @Param("countryCode") String countryCode,
            @Param("startDate") LocalDate startDate
    );

    @Query("SELECT o FROM OrderEntity o " +
            "WHERE o.status = :status " +
            "AND o.countryCode = :countryCode " +
            "AND o.createdAt >= :startDate " +
            "AND o.createdAt <= :endDate")
    Page<OrderEntity> findOrdersByStatusAndCountryCodeAndSellerIdAndBrandIdAndDateRange(
            @Param("status") ExternalStatus status,
            @Param("countryCode") String countryCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    OrderEntity findByOrderId(String orderId);
    OrderEntity findByOrderIdAndUserId(String orderId, String userId);

    List<OrderEntity> findAllByStatusAndScheduledDate(ExternalStatus status, Timestamp date);

    List<OrderEntity> findAllByOrderId(String orderId);

    List<OrderEntity> findByStatus(ExternalStatus status);


    List<OrderEntity> findByUserIdAndExternalStatusIn(String userId, List<ExternalStatus> ois);


}
