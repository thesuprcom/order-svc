package com.supr.orderservice.repository;

import com.supr.orderservice.entity.AccountingOrderItemEntity;
import com.supr.orderservice.enums.ExternalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AccountingOrderItemEntityRepository extends JpaRepository<AccountingOrderItemEntity, Long> {

    List<AccountingOrderItemEntity> findByStoreId(String storeId);

    List<AccountingOrderItemEntity> findByStoreIdAndCreatedAtBetweenAndExternalStatusIn(
            String storeId,
            Date startTime,
            Date endTime,
            List<ExternalStatus> externalStatusList);

    List<AccountingOrderItemEntity> findByStoreIdInAndCreatedAtBetweenAndExternalStatusIn(
            List<String> storeIds,
            Date startTime,
            Date endTime,
            List<ExternalStatus> externalStatusList);

    List<AccountingOrderItemEntity> findByStoreIdAndCreatedAtBetweenAndExternalStatusInAndInvoiceNumberIsNull(
            String storeId,
            Date startTime,
            Date endTime,
            List<ExternalStatus> externalStatusList);

    List<AccountingOrderItemEntity> findByExternalStatusInAndCreatedAtBetween(List<ExternalStatus> externalStatusList,
                                                                               Date startTime, Date endTime);

    List<AccountingOrderItemEntity> findByStoreIdAndCreatedAtBetween(String storeId, Date startTime, Date endTime);

    List<AccountingOrderItemEntity> findByCreatedAtBetween(Date startTime, Date endTime);

    List<AccountingOrderItemEntity> findByOrderId(String orderId);

    List<AccountingOrderItemEntity> findByInvoiceNumber(String invoiceId);

    AccountingOrderItemEntity findByOrderItemId(String orderItemId);

}
