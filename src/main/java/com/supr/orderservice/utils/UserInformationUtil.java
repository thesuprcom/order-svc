package com.supr.orderservice.utils;

import com.supr.orderservice.exception.AuthenticationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInformationUtil {
  public static String getUserId(HttpServletRequest httpRequest) {
    return Optional.ofNullable(httpRequest.getAttribute(Constants.USER_ID_ATTRIBUTE))
        .map(Object::toString)
        .orElseThrow(() -> new AuthenticationException(Constants.INVALID_USER));
  }

}
