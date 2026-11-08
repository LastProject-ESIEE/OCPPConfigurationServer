package fr.uge.ocpp.service.security;

import fr.uge.ocpp.database.dao.UserDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Security configuration of the REST API (JWT bearer authentication). */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  /**
   * Provides the security filter chain of the API.
   *
   * @param http the security builder.
   * @param jwtService the JWT service.
   * @param userDetailsService the user details service backed by the users table.
   * @return the security filter chain.
   * @throws Exception if the configuration fails.
   */
  @Bean
  SecurityFilterChain apiFilterChain(
      HttpSecurity http, JwtService jwtService, UserDetailsService userDetailsService)
      throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    // The Swagger UI, API docs and login endpoint stay public so users can
                    // authenticate from the Swagger page itself (Authorize button) and get a
                    // token from /api/login.
                    .requestMatchers("/api", "/v3/api-docs/**", "/swagger-ui/**", "/api/login")
                    .permitAll()
                    .requestMatchers("/api/**", "/h2-console/**")
                    .authenticated()
                    .requestMatchers("/", "/about")
                    .permitAll()
                    .anyRequest()
                    .permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        // JWT authentication is stateless: no HTTP session is created.
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Missing or invalid token -> 401 instead of the default 403.
        .exceptionHandling(
            handling ->
                handling.authenticationEntryPoint(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtService, userDetailsService),
            UsernamePasswordAuthenticationFilter.class)
        // The H2 console (local profile) renders in an iframe, so allow same-origin frames.
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
    return http.build();
  }

  /**
   * Provides the JWT service signing the access tokens.
   *
   * @param secret the HMAC secret (at least 32 bytes for HS256).
   * @param expirationMs the lifetime of a token in milliseconds.
   * @return the JWT service.
   */
  @Bean
  JwtService jwtService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long expirationMs) {
    return new JwtService(secret, expirationMs);
  }

  /**
   * Provides the user details service backed by the users table.
   *
   * @param userDao the Spring Data repository of users.
   * @return the user details service.
   */
  @Bean
  UserDetailsService userDetailsService(UserDao userDao) {
    return new UserDetailsServiceImpl(userDao);
  }

  /**
   * Provides the authentication provider of the API.
   *
   * @param userDetailsService the user details service.
   * @param passwordEncoder the password encoder.
   * @return the authentication provider.
   */
  @Bean
  DaoAuthenticationProvider authenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  /**
   * Provides the authentication manager used by the login endpoint.
   *
   * @param authenticationProvider the authentication provider.
   * @return the authentication manager.
   */
  @Bean
  AuthenticationManager authenticationManager(DaoAuthenticationProvider authenticationProvider) {
    return new ProviderManager(authenticationProvider);
  }

  /**
   * Provides the password encoder of the API.
   *
   * @return the password encoder.
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
