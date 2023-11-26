package com.supr.orderservice.service.external;

import com.supr.orderservice.model.request.UpdateQuantityRequest;
import com.supr.orderservice.model.response.UpdateQuantityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "catalog-update-service", url = "${misc-team.base-url}")
public interface CatalogUpdateClient {
    @PutMapping("/update_stock")
    UpdateQuantityResponse updateQuantity(UpdateQuantityRequest request);
}

