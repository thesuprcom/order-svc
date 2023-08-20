package com.supr.orderservice.repository;

import com.supr.orderservice.entity.StoreOrderInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOrderInvoiceRepository extends JpaRepository<StoreOrderInvoiceEntity, Long> {

  StoreOrderInvoiceEntity findByStoreIdAndOrderId(String storeId, String orderId);

  List<StoreOrderInvoiceEntity> findByOrderIdIn(List<String> orderIds);
}
