package com.supr.orderservice.controller;

import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.model.SavedCardDetails;
import com.supr.orderservice.utils.CardDetailsUtility;
import com.supr.orderservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-card")
@RequiredArgsConstructor
public class UserCardController {
    private final JwtTokenUtil jwtTokenUtil;
    private final CardDetailsUtility cardDetailsUtility;

    @GetMapping("list")
    ResponseEntity<SavedCardDetails> fetchAllUserCards(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String tokenWithoutBearer = token.split("Bearer")[1];
        String userId = jwtTokenUtil.getPublicUserId(tokenWithoutBearer);
        List<CardDetailsEntity> cardDetailsEntities = cardDetailsUtility.fetchCardDetailsList(userId);
        SavedCardDetails savedCardDetails = cardDetailsUtility.fetchSavedCardDetails(cardDetailsEntities);
        return ResponseEntity.ok(savedCardDetails);
    }

    @DeleteMapping("deleted/{card-id}")
    ResponseEntity<SavedCardDetails> deleteCard(@PathVariable("card-id") String cardId,
                                                HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String tokenWithoutBearer = token.split("Bearer")[1];
        String userId = jwtTokenUtil.getPublicUserId(tokenWithoutBearer);
        SavedCardDetails savedCardDetails = cardDetailsUtility.deleteCard(cardId, userId);
        return ResponseEntity.ok(savedCardDetails);
    }
}
