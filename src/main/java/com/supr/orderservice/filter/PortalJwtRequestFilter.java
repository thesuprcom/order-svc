package com.supr.orderservice.filter;

import com.google.gson.Gson;
import com.supr.orderservice.exception.PortalAuthExceptionResponse;
import com.supr.orderservice.model.Permission;
import com.supr.orderservice.service.external.PortalPermissionService;
import com.supr.orderservice.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.supr.orderservice.utils.PortalAuthConstants.AUTHORIZATION_HEADER;
import static com.supr.orderservice.utils.PortalAuthConstants.BEARER_PREFIX;
import static com.supr.orderservice.utils.PortalAuthConstants.JWT_TOKEN_HAS_EXPIRED;
import static com.supr.orderservice.utils.PortalAuthConstants.MALFORMED_JWT_TOKEN;
import static com.supr.orderservice.utils.PortalAuthConstants.PORTAL_AUTH_403;
import static com.supr.orderservice.utils.PortalAuthConstants.TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING;
import static com.supr.orderservice.utils.PortalAuthConstants.UNABLE_TO_GET_JWT_TOKEN;


public class PortalJwtRequestFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;

  private final PortalPermissionService portalPermissionService;

  private final Gson gson = new Gson();

  public PortalJwtRequestFilter(JwtTokenUtil jwtTokenUtil,
                                PortalPermissionService portalPermissionService) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.portalPermissionService = portalPermissionService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {

    Optional<String> requestTokenHeader = getHeader(request, AUTHORIZATION_HEADER);
    String username;
    String jwtToken;
    if (requestTokenHeader.isPresent() && requestTokenHeader.get().startsWith(BEARER_PREFIX)) {
      jwtToken = requestTokenHeader.get().substring(BEARER_PREFIX.length());
      try {
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        logger.error(e);
        setErrorHttpResponse(UNABLE_TO_GET_JWT_TOKEN, response);
        return;
      } catch (ExpiredJwtException e) {
        logger.error(e);
        setErrorHttpResponse(JWT_TOKEN_HAS_EXPIRED, response);
        return;
      } catch (MalformedJwtException | SignatureException e) {
        logger.error(e);
        setErrorHttpResponse(MALFORMED_JWT_TOKEN, response);
        return;
      }
    } else {
      setErrorHttpResponse(TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING, response);
      return;
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      List<Permission> permissions = portalPermissionService.getUserPermissions(requestTokenHeader.get());
      List<SimpleGrantedAuthority> authorities =
              permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getUid()))
                      .collect(Collectors.toList());
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(username, null, authorities);

      usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    chain.doFilter(request, response);
  }

  private void setErrorHttpResponse(String message, HttpServletResponse response) throws IOException {
    response.getWriter().write(getErrorBody(message));
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
    response.setStatus(HttpStatus.FORBIDDEN.value());
  }

  private Optional<String> getHeader(final HttpServletRequest request, final String headerName) {
    return Optional.ofNullable(request.getHeader(headerName));
  }

  private String getErrorBody(String message) {
    return gson.toJson(new PortalAuthExceptionResponse(PORTAL_AUTH_403, message));
  }


}