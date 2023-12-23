package com.supr.orderservice.utils;

import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;


@Service
public class JwtTokenUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${customer.jwt.secret}")
    private String customerSecret;


    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, secret, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token, String secret) {
        return getClaimFromToken(token, secret, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, String secret, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token, secret);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token, String secret) {
        final Date expiration = getExpirationDateFromToken(token, secret);
        return expiration.before(new Date());
    }

    public String getPublicUserId(String token) {
        Claims claims = getAllClaimsFromToken(token, customerSecret);
        String userId = claims.get("public_id", String.class);
        return userId;
    }

}
