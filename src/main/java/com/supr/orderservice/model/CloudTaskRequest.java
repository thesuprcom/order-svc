package com.supr.orderservice.model;

import com.google.cloud.tasks.v2.HttpMethod;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class CloudTaskRequest {
  String url;
  String body;
  String taskName;
  String scheduleAfter;
  HttpMethod httpMethod;
  Map<String, String> headers;
}