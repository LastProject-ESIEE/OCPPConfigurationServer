package fr.uge.chargepointconfiguration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for endpoints of backend.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  /*
  @Bean
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(User.withUsername("user")
      .password(passwordEncoder().encode("password"))
      .roles("USER")
      .build());
    manager.createUser(User.withUsername("admin")
      .password(passwordEncoder().encode("admin"))
      .roles("USER", "ADMIN")
      .build());
    return manager;
  }*/


  //  @Bean
  //  UserDetailsManager users(DataSource dataSource) {
  //    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
  //    return users;
  //  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize ->
                    authorize.requestMatchers("/api").authenticated()
                            .anyRequest().permitAll()
            )
            .formLogin(formLogin -> formLogin.loginPage("/testLogin")
                    .failureUrl("/authentication/login?failed")
                    .loginProcessingUrl("/authentication/login/process"))
            .httpBasic(Customizer.withDefaults())
            .headers(headers ->
                    headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable));
    return http.build();
  }

  @Bean
  InMemoryUserDetailsManager userDetailsService() {
    UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password")
            .roles("ADMIN")
            .build();
    UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(admin, user);
  }

  //  @Bean
  //  SecurityFilterChain web(HttpSecurity http) throws Exception {
  //    http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/**")
  //            .hasAnyAuthority("USER", "ADMIN")
  //            .anyRequest()
  //            .authenticated());
  //    return http.build();
  //  }

  //  @Bean
  //  public PasswordEncoder passwordEncoder() {
  //    return new BCryptPasswordEncoder();
  //  }

}
