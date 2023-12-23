package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

  OrderItemEntity findFirstByOrderItemId(String orderItemId);

  List<OrderItemEntity> findAllByOrderIdIn(List<Long> orderIds);
  List<OrderItemEntity> findAllByStatusAndOrderIdIn(OrderItemStatus status, List<Long> orderIds);

  @Query("SELECT o FROM OrderItemEntity o " +
          "WHERE o.productBrand = :brandCode " +
          "AND o.sellerId = :sellerId " +
          "AND o.countryCode = :countryCode " +
          "AND o.createdAt >= :startDate " +
          "AND o.createdAt <= :endDate")
  Page<OrderItemEntity> findOrdersByCountryCodeAndSellerIdAndBrandCodeAndDateRange(
          @Param("brandCode") String brandCode,
          @Param("sellerId") String sellerId,
          @Param("countryCode") String countryCode,
          @Param("startDate") Date startDate,
          @Param("endDate") Date endDate,
          Pageable pageable
  );

  @Query("SELECT o.externalStatus, COUNT(o) FROM OrderItemEntity o " +
          "WHERE o.productBrand = :brandCode " +
          "AND o.sellerId = :sellerId " +
          "AND o.countryCode = :countryCode " +
          "AND o.createdAt >= :startDate " +
          "AND o.externalStatus IN :statusList " +
          "GROUP BY o.externalStatus")
  List<Object[]> getOrderCountByStatusAndCountryCodeAndSellerIdAndBrandCodeAndDateGroupByStatus(
          @Param("brandCode") String brandCode,
          @Param("sellerId") String sellerId,
          @Param("countryCode") String countryCode,
          @Param("startDate") Date startDate,
          @Param("statusList") List<ExternalStatus> statusList
  );


  List<OrderItemEntity> findByOrderItemIdIn(List<String> orderItemIds);
  List<OrderItemEntity> findAllByExternalStatusAndSellerIdAndBrandCode(ExternalStatus status, String sellerId,
                                                               String brandCode);

  @Transactional
  @Modifying
  @Query(value = "delete from order_items where order_id = :orderId", nativeQuery = true)
  void deleteByOrderId(Long orderId);

}


