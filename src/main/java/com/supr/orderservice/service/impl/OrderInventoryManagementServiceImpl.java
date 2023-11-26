package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.model.request.UpdateQuantityRequest;
import com.supr.orderservice.model.response.UpdateQuantityResponse;
import com.supr.orderservice.service.OrderInventoryManagementService;
import com.supr.orderservice.service.external.CatalogUpdateClient;
import com.supr.orderservice.service.external.InventoryServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderInventoryManagementServiceImpl implements OrderInventoryManagementService {

    private final CatalogUpdateClient catalogUpdateClient;



    @Override
    public UpdateQuantityResponse updateStoreOrderQuantity(OrderEntity order) {
        log.info("Updating Item quantity for order {} ", order.getOrderId());
        UpdateQuantityRequest updateQuantityRequest = new UpdateQuantityRequest();
        updateQuantityRequest.setUpdatedBy(order.getUpdatedBy());
        updateQuantityRequest.setCountryCode(order.getCountryCode());
        List<UpdateQuantityRequest.DataItem> dataItems = new ArrayList<>();
        order.getOrderItemEntities().forEach(orderItemVO -> {
          UpdateQuantityRequest.DataItem data = new UpdateQuantityRequest.DataItem();
          data.setActionType("decrement");
          data.setStock(orderItemVO.getOrderItemQuantity().toString());
          data.setPskuCode(orderItemVO.getPskuCode());
          data.setSellerId(orderItemVO.getSellerId());
          dataItems.add(data);
        });
        return catalogUpdateClient.updateQuantity(updateQuantityRequest);
    }


}
