package com.supr.orderservice.service;

import com.supr.orderservice.model.GreetingCard;
import com.supr.orderservice.model.response.FetchGreetingResponse;

public interface InvitationService {
    FetchGreetingResponse handleInvitation(String orderId);
}
