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

    List<OrderEntity> findAllBySellerIdAndBrandIdAndCountryCodeAndExternalStatusIn(String sellerId, String brandId,
                                                                                   String countryCode,
                                                                                   List<ExternalStatus> statuses,
                                                                                   Pageable pageable);


    @Query("SELECT o.externalStatus, COUNT(o) FROM OrderEntity o " +
            "WHERE o.externalStatus IN :statusList " +
            "AND o.countryCode = :countryCode " +
            "AND o.sellerId = :sellerId " +
            "AND o.brandId = :brandId " +
            "AND o.createdAt >= :startDate " +
            "GROUP BY o.status")
    List<Object[]> getOrderCountByStatusAndCountryCodeAndSellerIdAndDateGroupByStatus(
            @Param("statusList") List<ExternalStatus> statusList,
            @Param("countryCode") String countryCode,
            @Param("sellerId") String sellerId,
            @Param("brandId") String brandId,
            @Param("startDate") LocalDate startDate
    );

    @Query("SELECT o FROM OrderEntity o " +
            "WHERE o.status = :status " +
            "AND o.countryCode = :countryCode " +
            "AND o.sellerId = :sellerId " +
            "AND o.brandId = :brandId " +
            "AND o.createdAt >= :startDate " +
            "AND o.createdAt <= :endDate")
    Page<OrderEntity> findOrdersByStatusAndCountryCodeAndSellerIdAndBrandIdAndDateRange(
            @Param("status") ExternalStatus status,
            @Param("countryCode") String countryCode,
            @Param("sellerId") String sellerId,
            @Param("brandId") String brandId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    OrderEntity findByOrderId(String orderId);

    List<OrderEntity> findAllByStatusAndScheduledDate(ExternalStatus status, Timestamp date);

    List<OrderEntity> findAllByOrderId(String orderId);

    List<OrderEntity> findByStatusAndSellerIdAndBrandId(ExternalStatus status, String sellerId, String brandId);

    Optional<OrderEntity> findByOrderIdAndStatusNot(String orderId, OrderItemStatus status);

    OrderEntity findByOrderIdAndSellerId(String orderId, String sellerId);

    Page<OrderEntity> findBySellerIdAndOrderPlacedTimeBetweenAndExternalStatusIn(
            String sellerId, Date startTime, Date endTime, List<ExternalStatus> statusList, Pageable page);

    Page<OrderEntity> findByUserIdAndCountryCodeAndExternalStatusIsNotNull(String userId,
                                                                           String countryCode, Pageable pageable);

    Page<OrderEntity> findBySellerIdAndExternalStatusIsNotNull(String sellerId, Pageable pageable);

    List<OrderEntity> findByUserIdAndExternalStatusIn(String userId, List<ExternalStatus> ois);

    Page<OrderEntity> findByExternalStatusIn(List<ExternalStatus> externalStatuses, Pageable pageable);

    Page<OrderEntity> findBySellerIdAndExternalStatusIn(String userId, List<ExternalStatus> statusList, Pageable pageable);

    Page<OrderEntity> findBySellerIdAndOrderIdIn(String sellerId, List<String> orderId, Pageable pageable);

    OrderEntity findByOrderIdAndUserIdAndCountryCode(String orderId, String userId, String countryCode);

    @Query("SELECT m.externalStatus AS name, COUNT(m) AS total " +
            "FROM OrderEntity AS m where m.sellerId = :sellerId  and m.externalStatus in :externalStatuses GROUP BY m.externalStatus")
    List<Object[]> fetchOrderCountBySellerIdAndExternalStatus(String sellerId, List<ExternalStatus> externalStatuses);

    @Query("SELECT COUNT(m) AS total " +
            "FROM OrderEntity AS m where m.userId = :userId  and m.status in :statusList")
    List<Object[]> fetchOrderCountByUserIdAndStatusIn(String userId, List<OrderItemStatus> statusList);

    @Query(
            "SELECT m.externalStatus AS name, COUNT(m) AS total " +
                    "FROM OrderEntity AS m where m.sellerId = :sellerId  and m.externalStatus in :externalStatuses " +
                    "and unix_timestamp(m.orderPlacedTime) >= :startTime " +
                    "and unix_timestamp(m.orderPlacedTime) <= :endTime GROUP BY m.externalStatus")
    List<Object[]> fetchOrderCountBySellerIdAndExternalStatusAndPlacedAtBetween(String sellerId,
                                                                               List<ExternalStatus> externalStatuses,
                                                                               long startTime, long endTime);

    @Query(value = "SELECT m.externalStatus AS name, COUNT(m) AS total FROM OrderEntity AS m " +
            "where m.countryCode = :countryCode and m.externalStatus in :externalStatuses GROUP BY m.externalStatus")
    List<Object[]> fetchOrderCountByExternalStatus(String countryCode, List<ExternalStatus> externalStatuses);


    @Query(
            "SELECT m.externalStatus AS name, COUNT(m) AS total " +
                    "FROM OrderEntity AS m where m.countryCode = :countryCode and  m.externalStatus in :externalStatuses " +
                    "and m.orderPlacedTime >= :startTime " +
                    "and m.orderPlacedTime <= :endTime GROUP BY m.externalStatus")
    List<Object[]> fetchOrderCountByExternalStatusAndPlacedAtBetween(String countryCode,
                                                                     List<ExternalStatus> externalStatuses,
                                                                     Date startTime, Date endTime);

    @Query(
            "SELECT m.externalStatus AS name, COUNT(m) AS total " +
                    "FROM OrderEntity AS m where m.countryCode = :countryCode and m.deliveryType = :deliveryType " +
                    "and m.externalStatus in :externalStatuses " +
                    "and m.orderPlacedTime >= :startTime " +
                    "and m.orderPlacedTime <= :endTime GROUP BY m.externalStatus")
    List<Object[]> fetchOrderCountByDeliveryTypeAndExternalStatusAndPlacedAtBetween(String countryCode,
                                                                                    String deliveryType,
                                                                                    List<ExternalStatus> externalStatuses,
                                                                                    Date startTime, Date endTime);

    @Query(
            "SELECT m.externalStatus AS name, COUNT(m) AS total " +
                    "FROM OrderEntity AS m where m.countryCode = :countryCode and m.deliveryType = :deliveryType " +
                    "and m.sellerId in :sellerIds " +
                    "and m.externalStatus in :externalStatuses " +
                    "and m.orderPlacedTime >= :startTime " +
                    "and m.orderPlacedTime <= :endTime GROUP BY m.externalStatus")
    List<Object[]> fetchOrderCountByDeliveryTypeAndExternalStatusAndSellerIdsAndPlacedAtBetween(String countryCode,
                                                                                               String deliveryType,
                                                                                               List<String> sellerIds,
                                                                                               List<ExternalStatus>
                                                                                                       externalStatuses,
                                                                                               Date startTime,
                                                                                               Date endTime);

    @Query(
            "SELECT m.externalStatus AS name, COUNT(m) AS total " +
                    "FROM OrderEntity AS m where m.countryCode = :countryCode and m.sellerId in :sellerIds and " +
                    "m.externalStatus in :externalStatuses " +
                    "and m.orderPlacedTime >= :startTime " +
                    "and m.orderPlacedTime <= :endTime GROUP BY m.externalStatus")
    List<Object[]> fetchOrderCountByExternalStatusAndSellerIdsAndPlacedAtBetween(String countryCode,
                                                                                List<String> sellerIds,
                                                                                List<ExternalStatus> externalStatuses,
                                                                                Date startTime, Date endTime);

    OrderEntity findByTrackingId(String trackingId);

    @Query(
            value = "Select :start + (((Unix_timestamp(o.order_placed_time) - :start) div :seconds) * :seconds) as ts, " +
                    "count(1) FROM user_orders as o WHERE unix_timestamp(o.order_placed_time) >= :start"
                    + " AND country_code = :countryCode AND external_status is not null " +
                    "and external_status <> 'FAILED' group by ts",
            nativeQuery = true)
    List<Object[]> fetchOrderCount(String countryCode, int seconds, long start);

    @Query(value = "SELECT :start + (((Unix_timestamp(o.order_placed_time) - :start) div :seconds) * :seconds) as ts," +
            "sum(o.total_amount) " +
            " FROM user_orders as o WHERE unix_timestamp(o.order_placed_time) >= :start" +
            " AND country_code = :countryCode AND external_status is not null " +
            "and external_status <> 'FAILED' group by ts",
            nativeQuery = true)
    List<Object[]> fetchOrderSales(String countryCode, int seconds, long start);

    List<OrderEntity> findBySellerIdAndOrderPlacedTimeBetween(String sellerId, Date startTime, Date endTime);

    List<OrderEntity> findBySellerIdAndOrderPlacedTimeBetweenAndExternalStatusIn(String sellerId, Date startTime, Date endTime,
                                                                                List<ExternalStatus> externalStatuses);

    List<OrderEntity> findByOrderPlacedTimeBetween(Date startTime, Date endTime);

    List<OrderEntity> findByOrderPlacedTimeBetweenAndExternalStatusIn(Date startTime, Date endTime,
                                                                      List<ExternalStatus> externalStatuses);

    Page<OrderEntity> findByCountryCodeAndStatusIn(String countryCode, List<OrderItemStatus> orderStatuses,
                                                   Pageable page);

    Page<OrderEntity> findByCountryCodeAndExternalStatusInAndUpdatedAtBefore(String countryCode,
                                                                             List<ExternalStatus> externalStatuses, Date date,
                                                                             Pageable page);

    Page<OrderEntity> findByCountryCodeAndExternalStatusIn(String countryCode, List<ExternalStatus> externalStatusList,
                                                           Pageable page);

    Page<OrderEntity> findByCountryCodeAndSellerIdInAndExternalStatusIn(String countryCode, List<String> sellerIds,
                                                                       List<ExternalStatus> externalStatusList, Pageable page);

    OrderEntity findTopByUserIdAndExternalStatusInOrderByCreatedAtDesc(String userId, ExternalStatus[] externalStatuses);

    List<OrderEntity> findByUserIdAndExternalStatus(String userId, ExternalStatus externalStatus);

    List<OrderEntity> findByExternalStatusAndUpdatedAtBetween(ExternalStatus externalStatus, Date startDate, Date endDate);

    boolean existsByUserIdAndOrderIdAndExternalStatus(String userId, String orderId, ExternalStatus externalStatus);

    boolean existsByUserIdAndStatusNotIn(String userId, List<OrderItemStatus> externalStatuses);

    @Modifying
    @Transactional
    void deleteByOrderId(String orderId);

    Optional<OrderEntity> findByOrderIdAndStatusIn(String orderId, List<OrderItemStatus> statuses);

    Optional<OrderEntity> findTop1ByUserIdAndStatus(String userId, OrderItemStatus status);

    Optional<OrderEntity> findTop1ByUserIdAndSubscriptionPendingTrueOrderByUpdatedAtDesc(String userId);

    List<OrderEntity> findByUserIdAndPaymentAuthorizationFailedCountGreaterThanAndStatusIn(
            String userId, int paymentFailedCount, List<OrderItemStatus> orderItemStatuses);

    List<OrderEntity> findByUserIdAndSubscriptionPendingTrue(String userId);

    Optional<OrderEntity> findByOrderIdAndSubscriptionPendingTrue(String orderId);

    List<OrderEntity> findAllByOrderIdIn(List<String> orderIds);

    OrderEntity findByOrderIdAndUserId(String orderId, String userId);


    List<OrderEntity> findBySellerIdAndExternalStatus(String sellerId, ExternalStatus externalStatus);
}
