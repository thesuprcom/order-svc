package com.supr.orderservice.controller;

import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.SubscriptionPageContext;
import com.supr.orderservice.model.SavedCardDetails;
import com.supr.orderservice.model.request.PaymentRequest;
import com.supr.orderservice.model.request.SubscriptionRequest;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.service.PaymentGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.supr.orderservice.utils.Constants.INTERNAL_ACCESS_TOKEN_HEADER;
import static com.supr.orderservice.utils.Constants.ORDER_SERVICE_EXTERNAL_API_PREFIX;
import static com.supr.orderservice.utils.Constants.SIGNATURE_HEADER;
import static com.supr.orderservice.utils.Constants.X_COUNTRY_CODE_HEADER_KEY;
import static com.supr.orderservice.utils.Constants.X_PAGE_CONTEXT;
import static com.supr.orderservice.utils.UserInformationUtil.getUserId;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ORDER_SERVICE_EXTERNAL_API_PREFIX)
public class PaymentSubscriptionController {
  private final PaymentGatewayService paymentSubscriptionService;

  @GetMapping("/subscription/payment-details")
  public ResponseEntity<SavedCardDetails> getPaymentDetails(HttpServletRequest httpServletRequest) {
    return ResponseEntity.ok(paymentSubscriptionService.paymentDetails(getUserId(httpServletRequest)));
  }

  @SneakyThrows
  @Transactional
  @PostMapping("/subscribe")
  public PaymentProcessingResponse subscribe(@Valid @RequestBody SubscriptionRequest subscriptionRequest,
                                             @RequestHeader(X_COUNTRY_CODE_HEADER_KEY) String countryCode,
                                             @RequestHeader(value = X_PAGE_CONTEXT, required = false)
                                               SubscriptionPageContext pageContext,
                                             HttpServletRequest httpServletRequest) {
    final String userId = getUserId(httpServletRequest);
    return paymentSubscriptionService.subscribe(subscriptionRequest, userId, countryCode, pageContext);
  }

  @PostMapping("/unsubscribe")
  public void unsubscribe(HttpServletRequest httpServletRequest) {
    paymentSubscriptionService.unsubscribe(getUserId(httpServletRequest));
  }

  @GetMapping("/callback/update/subscription/{payment-mode}/{country-code}")
  public void updateSubscription(@PathVariable("payment-mode") PaymentMode paymentMode,
                                 @PathVariable("country-code") String countryCode,
                                 @RequestParam("orderId") String pgOrderId) {
    try {
      paymentSubscriptionService.updateSubscription(paymentMode, countryCode, pgOrderId);
    } catch (Exception exception) {
      // Swallow this exception
      log.error("Exception occurred while updating the subscription: {}", exception);
    }
  }

  @PostMapping("/capture-payment")
  public void capturePayment(@RequestBody PaymentRequest paymentRequest,
                             @RequestHeader(INTERNAL_ACCESS_TOKEN_HEADER) String accessToken,
                             @RequestHeader(SIGNATURE_HEADER) String signature) {
    paymentSubscriptionService.capturePayment(paymentRequest, accessToken, signature);
  }

  @PostMapping("/refund-payment")
  public void refundPayment(@RequestBody PaymentRequest paymentRequest,
                            @RequestHeader(INTERNAL_ACCESS_TOKEN_HEADER) String accessToken,
                            @RequestHeader(SIGNATURE_HEADER) String signature) {
    paymentSubscriptionService.refundPayment(paymentRequest, accessToken, signature);
  }

  @SneakyThrows
  @PostMapping("/authorize-payment")
  public void authorizePaymentForUserOrder(@RequestBody PaymentRequest paymentRequest,
                                           @RequestHeader(INTERNAL_ACCESS_TOKEN_HEADER) String accessToken,
                                           @RequestHeader(SIGNATURE_HEADER) String signature) {
    paymentSubscriptionService.authorizePaymentForUserOrder(paymentRequest, accessToken, signature);
  }
}
