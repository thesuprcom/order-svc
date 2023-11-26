package com.supr.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.NextAction;
import com.supr.orderservice.enums.PaymentReason;
import com.supr.orderservice.model.CloudTaskRequest;
import com.supr.orderservice.model.request.PaymentRequest;
import com.supr.orderservice.repository.OrderRepository;
import com.supr.orderservice.service.external.NotificationServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.google.cloud.tasks.v2.HttpMethod.POST;
import static com.supr.orderservice.utils.Constants.AUTHORIZE_PAYMENT_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.CANCEL_USER_ORDER_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.CAPTURE_PAYMENT_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.CREATE_SHIPMENT_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.ORDER_SERVICE_EXTERNAL_API_PREFIX;
import static com.supr.orderservice.utils.Constants.PLACE_ORDER_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.REFUND_PAYMENT_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.RETRY_AUTHORIZE_PAYMENT_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.SCHEDULED_CANCEL_USER_ORDER_TASK_NAME_PREFIX;
import static com.supr.orderservice.utils.Constants.SCHEDULE_AFTER_TEN_SECONDS;
import static com.supr.orderservice.utils.Constants.SCHEDULE_AFTER_ZERO_SECOND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudTaskEnqueueService {
  private final ObjectMapper objectMapper;
  private final OrderRepository orderRepository;
  private final NotificationServiceClient notificationServiceClient;

  @Value("${app.basic-auth-token}")
  private String accessToken;

  @Value("${order.base-url}")
  private URL orderServiceUrl;

  @Value("${app.change-payment-subscription.timer.duration}")
  private String scheduleCancelOrderAfter;


  @Retryable(value = {Exception.class, FeignException.class})
  public void enqueueNextAction(final NextAction nextAction, final PaymentRequest paymentRequest) {
    switch (nextAction) {
      case PLACE_ORDER:
        enqueuePlaceOrderTask(paymentRequest);
        break;
      case SUBSCRIPTION_CAPTURE:
        enqueueSubscriptionCapturePaymentAction(paymentRequest);
        break;
      case SUBSCRIPTION_REFUND:
        enqueueSubscriptionRefundAmountAction(paymentRequest);
        break;
      case AUTHORIZE_PAYMENT_FOR_USER_ORDER:
        final String taskName = AUTHORIZE_PAYMENT_TASK_NAME_PREFIX + paymentRequest.getOrderId();
        enqueueAuthorizePaymentForUserOrder(taskName, SCHEDULE_AFTER_TEN_SECONDS, paymentRequest);
        break;
      case RETRY_AUTHORIZE_PAYMENT_FOR_USER_ORDER:
        String retryAuthorizationTaskName = RETRY_AUTHORIZE_PAYMENT_TASK_NAME_PREFIX + paymentRequest.getOrderId();
        enqueueAuthorizePaymentForUserOrder(retryAuthorizationTaskName, SCHEDULE_AFTER_ZERO_SECOND, paymentRequest);
        break;
      case CAPTURE_PAYMENT_FOR_USER_ORDER:
        enqueueCapturePaymentActionForUserOrder(paymentRequest);
        break;
      case CREATE_SHIPMENT:
        enqueueCreateShipmentTask(paymentRequest);
        break;
      case CANCEL_USER_ORDER:
        final String cancelUserOrderTaskName = CANCEL_USER_ORDER_TASK_NAME_PREFIX + paymentRequest.getOrderId();
        enqueueCancelUserOrder(cancelUserOrderTaskName, paymentRequest, SCHEDULE_AFTER_ZERO_SECOND);
        break;
      case SCHEDULE_CANCEL_USER_ORDER:
        String scheduleCancelUserOrderTaskName = SCHEDULED_CANCEL_USER_ORDER_TASK_NAME_PREFIX +
            paymentRequest.getOrderId();
        enqueueCancelUserOrder(scheduleCancelUserOrderTaskName, paymentRequest, scheduleCancelOrderAfter);
        break;
    }
  }

  @SneakyThrows
  private void enqueueCreateShipmentTask(final PaymentRequest paymentRequest) {
    String orderId = paymentRequest.getOrderId();
    String apiPath = new URL(orderServiceUrl, ORDER_SERVICE_EXTERNAL_API_PREFIX +
        "/create-shipment/" + orderId).toString();

    final CloudTaskRequest cloudTaskRequest = createCloudTaskRequest(CREATE_SHIPMENT_TASK_NAME_PREFIX + orderId,
        paymentRequest.getUserId(), apiPath, null);

    log.info("Creating shipment for order id: {}", orderId);
    notificationServiceClient.createCloudTask(cloudTaskRequest);
  }

  @SneakyThrows
  private void enqueueCancelUserOrder(final String taskName,
                                      final PaymentRequest paymentRequest,
                                      final String scheduleAfter) {
    String orderId = paymentRequest.getOrderId();
    String apiPath = new URL(orderServiceUrl, ORDER_SERVICE_EXTERNAL_API_PREFIX +
        "/payment-failure/fail-order/" + orderId).toString();

    final CloudTaskRequest cloudTaskRequest = createCloudTaskRequest(taskName, paymentRequest.getUserId(), apiPath,
        scheduleAfter, null);

    log.info("Cancelling user order: {}", orderId);
    notificationServiceClient.createCloudTask(cloudTaskRequest);
  }

  @SneakyThrows
  private void enqueueSubscriptionCapturePaymentAction(PaymentRequest paymentRequest) {
    String apiPath = getCapturePaymentApiPath();
    String pgOrderId = paymentRequest.getPgOrderId();

    PaymentRequest subscriptionCapturePaymentRequest = buildPaymentRequest(pgOrderId, paymentRequest.getUserId(),
        paymentRequest.getCurrency(), Lists.newArrayList(NextAction.SUBSCRIPTION_REFUND));

    final CloudTaskRequest cloudTaskRequest = createCloudTaskRequest(CAPTURE_PAYMENT_TASK_NAME_PREFIX + pgOrderId,
        paymentRequest.getUserId(), apiPath, objectMapper.writeValueAsString(subscriptionCapturePaymentRequest));

    log.info("Enqueuing cloud task to capture amount for pgOrder id: {}", pgOrderId);
    notificationServiceClient.createCloudTask(cloudTaskRequest);
  }

  @SneakyThrows
  private void enqueueCapturePaymentActionForUserOrder(final PaymentRequest paymentRequest) {
    String apiPath = getCapturePaymentApiPath();

    final PaymentRequest capturePaymentRequest = buildPaymentRequestForUserOrderCapture(paymentRequest);

    String orderId = paymentRequest.getOrderId();
    String taskName = CAPTURE_PAYMENT_TASK_NAME_PREFIX + orderId;
    final String body = objectMapper.writeValueAsString(capturePaymentRequest);

    CloudTaskRequest cloudTaskRequest = createCloudTaskRequest(taskName, paymentRequest.getUserId(), apiPath, body);

    log.info("Enqueuing cloud task to capture amount for order id: {}", orderId);
    notificationServiceClient.createCloudTask(cloudTaskRequest);
  }

  private String getCapturePaymentApiPath() throws MalformedURLException {
    return new URL(orderServiceUrl, ORDER_SERVICE_EXTERNAL_API_PREFIX + "/capture-payment").toString();
  }

  @SneakyThrows
  private void enqueueSubscriptionRefundAmountAction(final PaymentRequest paymentRequest) {
    String pgOrderId = paymentRequest.getPgOrderId();
    PaymentRequest subscriptionRefundPaymentRequest = buildPaymentRequest(pgOrderId, paymentRequest.getUserId(),
        paymentRequest.getCurrency(), Lists.newArrayList());

    String apiPath = new URL(orderServiceUrl, ORDER_SERVICE_EXTERNAL_API_PREFIX + "/refund-payment").toString();

    CloudTaskRequest cloudTaskRequest = createCloudTaskRequest(REFUND_PAYMENT_TASK_NAME_PREFIX + pgOrderId,
        paymentRequest.getUserId(), apiPath, objectMapper.writeValueAsString(subscriptionRefundPaymentRequest));

    log.info("Enqueuing cloud task to refund amount for pgOrder id: {}", pgOrderId);
    notificationServiceClient.createCloudTask(cloudTaskRequest);
  }

  @SneakyThrows
  private void enqueuePlaceOrderTask(final PaymentRequest paymentRequest) {
    String orderId = paymentRequest.getOrderId();
    String apiPath = new URL(orderServiceUrl, ORDER_SERVICE_EXTERNAL_API_PREFIX + "/place-order/" + orderId)
        .toString();
    final CloudTaskRequest cloudTaskRequest = createCloudTaskRequest(PLACE_ORDER_TASK_NAME_PREFIX + orderId,
        paymentRequest.getUserId(), apiPath, null);

    log.info("Enqueuing cloud task to place order for order id: {}", orderId);
    notificationServiceClient.createCloudTask(cloudTaskRequest);
  }


  @SneakyThrows
  private void enqueueAuthorizePaymentForUserOrder(final String taskName,
                                                   final String scheduleAfter,
                                                   final PaymentRequest paymentRequest) {
    String apiPath = new URL(orderServiceUrl, ORDER_SERVICE_EXTERNAL_API_PREFIX + "/authorize-payment").toString();
    String orderId = paymentRequest.getOrderId();
    final String body = objectMapper.writeValueAsString(paymentRequest);

    CloudTaskRequest cloudTaskRequest =
        createCloudTaskRequest(taskName, paymentRequest.getUserId(), apiPath, scheduleAfter, body);

    log.info("Enqueuing cloud task to authorize payment for user order: {}", orderId);
    notificationServiceClient.createCloudTask(cloudTaskRequest);
  }

  private CloudTaskRequest createCloudTaskRequest(final String taskName,
                                                  final String userId,
                                                  final String apiPath,
                                                  final String scheduleAfter,
                                                  final String body) {
    return CloudTaskRequest.builder()
        .taskName(taskName)
        .url(apiPath)
        .headers(null)
        .httpMethod(POST)
        .scheduleAfter(scheduleAfter)
        .body(body)
        .build();
  }

  private CloudTaskRequest createCloudTaskRequest(final String taskName,
                                                  final String userId,
                                                  final String apiPath,
                                                  final String body) {
    return createCloudTaskRequest(taskName, userId, apiPath, SCHEDULE_AFTER_ZERO_SECOND, body);
  }

  private PaymentRequest buildPaymentRequest(final String pgOrderId,
                                             final String userId,
                                             final String currency,
                                             final List<NextAction> nextActions) {
    return PaymentRequest.builder()
        .pgOrderId(pgOrderId)
        .userId(userId)
        .amount(BigDecimal.ONE)
        .currency(currency)
        .paymentReason(PaymentReason.SUBSCRIPTION)
        .nextActions(nextActions)
        .build();
  }

  private PaymentRequest buildPaymentRequestForUserOrderCapture(final PaymentRequest parentPaymentRequest) {
    final String orderId = parentPaymentRequest.getOrderId();
    final OrderEntity order = orderRepository.findByOrderId(orderId);

    return PaymentRequest.builder()
        .orderId(orderId)
        .userId(parentPaymentRequest.getUserId())
        .pgOrderId(order.getTransaction().getPgOrderId())
        .currency(order.getCurrencyCode())
        .paymentReason(PaymentReason.USER_ORDER)
        .nextActions(Lists.newArrayList(NextAction.CREATE_SHIPMENT))
        .amount(order.getPrice().getTotalPrice())
        .build();
  }
}
