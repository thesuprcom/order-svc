package com.supr.orderservice.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.supr.orderservice.utils.Constants.ORDER_SERVICE_EXTERNAL_API_PREFIX;

public class WebMvcConfig implements WebMvcConfigurer {

    private final HandlerInterceptor authenticationInterceptor;

    public WebMvcConfig(@Lazy HandlerInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/v1/customer/order/**")
                .addPathPatterns("/order-service/external/api/v1/customer/subscription-order/**")
                .addPathPatterns("/order-service/external/api/v1/push-notification/**")
                .addPathPatterns("/api/v1/customer/payment-info/**")
                .addPathPatterns(ORDER_SERVICE_EXTERNAL_API_PREFIX + "/new-subscription-status/**")
                .addPathPatterns(ORDER_SERVICE_EXTERNAL_API_PREFIX + "/subscription/payment-details/**")
                .addPathPatterns(ORDER_SERVICE_EXTERNAL_API_PREFIX + "/subscribe/**")
                .addPathPatterns(ORDER_SERVICE_EXTERNAL_API_PREFIX + "/unsubscribe/**")
                .addPathPatterns(ORDER_SERVICE_EXTERNAL_API_PREFIX + "/reschedule-slotted-order");
    }
}
