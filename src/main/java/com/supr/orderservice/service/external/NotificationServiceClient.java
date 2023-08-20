package com.supr.orderservice.service.external;

import com.supr.orderservice.model.CloudTaskRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "notification-service", url = "${notification-service.base-url}")
public interface NotificationServiceClient {
  @PostMapping("/cloud-task")
  void createCloudTask(@RequestBody CloudTaskRequest cloudTaskRequest);

  @DeleteMapping("/cloud-task")
  void deleteCloudTask(@Valid @RequestBody String taskNameToDelete);
}
