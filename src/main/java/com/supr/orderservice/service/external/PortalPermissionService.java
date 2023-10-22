package com.supr.orderservice.service.external;

import com.supr.orderservice.model.BackendRoutesPermission;
import com.supr.orderservice.model.Permission;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "backend-routes-permissions", url = "${portal-auth.base-url}")
public interface PortalPermissionService {

    @GetMapping("/v1/permissions")
    List<Permission> getPermissions();

    @GetMapping("/v1/user-permissions")
    List<Permission> getUserPermissions(@RequestHeader(value = "Authorization") String token);

    @GetMapping("/v1/backend-route-permissions")
    List<BackendRoutesPermission> getBackendRoutePermissions();

}