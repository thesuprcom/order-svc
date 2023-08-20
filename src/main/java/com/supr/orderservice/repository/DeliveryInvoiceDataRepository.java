package com.supr.orderservice.repository;

import com.supr.orderservice.entity.DeliveryInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryInvoiceDataRepository extends JpaRepository<DeliveryInvoiceEntity, Long> {

  DeliveryInvoiceEntity findDeliveryInvoiceDataByOrderId(String orderId);

  List<DeliveryInvoiceEntity> findAllByIdIn(List<Long> ids);

  @Query(value = "select id from delivery_invoice_data where id != substring(invoice_number, 21, 6)",
      nativeQuery = true)
  List<Long> findIncorrectInvoiceIds();
}
