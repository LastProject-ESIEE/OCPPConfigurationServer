package fr.uge.chargepointconfiguration.security;

import fr.uge.chargepointconfiguration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for endpoints of backend.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

  @Bean
  SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(authorize ->
                    authorize.requestMatchers("/api/**").authenticated()
                            .requestMatchers("/index.html").permitAll()
                            // allow React to access its files
                            .requestMatchers("/static/**").permitAll()
                            // allow React to access its files
                            .requestMatchers("/manifest.json").permitAll()
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/static/**").permitAll()
                            .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin.loginPage("/login").permitAll()
                    .failureUrl("/login?failed")
                    .defaultSuccessUrl("/api", true)
                    // see : https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configurers/AbstractAuthenticationFilterConfigurer.html#defaultSuccessUrl(java.lang.String,boolean)
                    .loginProcessingUrl("/authentication/login/process"))
            .formLogin(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable) // TODO csrf propre
            .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  static RoleHierarchy roleHierarchy() {
    var hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy("""
            ROLE_ADMIN > ROLE_USER
            """);
    return hierarchy;
  }

  // and, if using method security also add
  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
          RoleHierarchy roleHierarchy) {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy);
    return expressionHandler;
  }

  @Bean
  UserDetailsService userDetailsService(UserRepository userRepository) {
    return new UserDetailsServiceImpl(userRepository);
  }

  /**
   * Provides the authentication service for the app to let users connects thanks to their username.
   *
   * @param userRepository The user's repository.
   * @return An authentication provider for the user.
   */
  @Autowired
  @Bean
  public DaoAuthenticationProvider authenticationProvider(UserRepository userRepository) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService(userRepository));
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
