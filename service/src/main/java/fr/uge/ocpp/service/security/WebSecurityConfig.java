package fr.uge.ocpp.service.security;

import fr.uge.ocpp.database.repositories.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** Security configuration of the REST API. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  /**
   * Provides the security filter chain of the API.
   *
   * @param http the security builder.
   * @return the security filter chain.
   * @throws Exception if the configuration fails.
   */
  @Bean
  SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/api/**")
                    .authenticated()
                    .requestMatchers("/", "/about", "/v3/api-docs/**", "/swagger-ui/**")
                    .permitAll()
                    .anyRequest()
                    .permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(httpBasic -> {});
    return http.build();
  }

  /**
   * Removes the default ROLE_ prefix expected by the authority expressions.
   *
   * @return the granted authority defaults.
   */
  @Bean
  static GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  /**
   * Provides the user details service backed by the users table.
   *
   * @param userJpaRepository the Spring Data repository of users.
   * @return the user details service.
   */
  @Bean
  UserDetailsService userDetailsService(UserJpaRepository userJpaRepository) {
    return new UserDetailsServiceImpl(userJpaRepository);
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
   * Provides the password encoder of the API.
   *
   * @return the password encoder.
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
