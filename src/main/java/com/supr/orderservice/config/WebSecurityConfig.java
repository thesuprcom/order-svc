package com.supr.orderservice.config;

import com.supr.orderservice.filter.PortalJwtRequestFilter;
import com.supr.orderservice.service.external.PortalPermissionService;
import com.supr.orderservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String OMS_USERS_UID = "2679a0d1-15d8-4058-9e18-5c6d55ea62b5";
    private static final String[] SKIP_AUTH_URLS =
            new String[]{"/health/**", "/actuator/**", "/configuration/ui/**", "/swagger-resources/**",
                    "/configuration/security/**", "/swagger-ui.html/**", "/webjars/**", "/swagger-ui/**",
                    "/v2/api-docs/**", "/healthcheck/", "/v1/internal/**","/api/v1/internal/sender/**"};
    private final PortalPermissionService portalPermissionService;
    private final JwtTokenUtil jwtTokenUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(SKIP_AUTH_URLS);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/v1/internal/**", "/healthcheck", "/actuator/health").permitAll() // Exclude this URL from the filter chain
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new PortalJwtRequestFilter(jwtTokenUtil, portalPermissionService),
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}