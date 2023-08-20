package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLiteRepository extends JpaRepository<OrderEntity, Long> {

  @Query(value = "SELECT * FROM user_orders  where country_code = :countryCode and "
      + "external_status in :externalStatusList order by order_placed_time desc limit :limit offset :offset",
      nativeQuery = true)
  List<OrderEntity> findByCountryCodeAndExternalStatusIn(String countryCode,
                                                       List<String> externalStatusList,
                                                       int limit, long offset);

  @Query(value = "SELECT * FROM user_orders  where country_code = :countryCode and "
      + "external_status in :externalStatusList and store_id in :storeIds order by order_placed_time "
      + "desc limit :limit offset :offset",
      nativeQuery = true)
  List<OrderEntity> findByCountryCodeAndExternalStatusInAndStoreId(String countryCode, List<String> storeIds,
                                                       List<String> externalStatusList,
                                                       int limit, long offset);

  @Query(value = "SELECT * FROM user_orders  where country_code = :countryCode and "
      + "delivery_type = :deliveryType and external_status in :externalStatusList and "
      + "store_id in :storeIds order by order_placed_time "
      + "desc limit :limit offset :offset",
      nativeQuery = true)
  List<OrderEntity> findByCountryCodeAndDeliveryTypeAndExternalStatusInAndStoreId(String countryCode, String deliveryType,
                                                                            List<String> storeIds,
                                                                            List<String> externalStatusList,
                                                                            int limit, long offset);

  @Query(value = "SELECT * FROM user_orders  where country_code = :countryCode and delivery_type = :deliveryType and "
          + "external_status in :externalStatusList order by order_placed_time desc limit :limit offset :offset",
          nativeQuery = true)
  List<OrderEntity> findByCountryCodeAndDeliveryTypeAndExternalStatusIn(String countryCode, String deliveryType,
                                                                  List<String> externalStatusList,
                                                                  int limit, long offset);
}