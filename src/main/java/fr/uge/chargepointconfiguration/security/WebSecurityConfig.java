package fr.uge.chargepointconfiguration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(authorize ->
                    //                    authorize.requestMatchers("/api/**").authenticated()
                    //                            .requestMatchers("/login").permitAll()
                    //                            .requestMatchers("/index.html").permitAll()
                    //                            .anyRequest().authenticated()
                    authorize.anyRequest().permitAll()
            )
            .formLogin(formLogin -> formLogin.loginPage("/login")
                    .failureUrl("/login?failed")
                    .defaultSuccessUrl("/youpii")
                    // see : https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configurers/AbstractAuthenticationFilterConfigurer.html#defaultSuccessUrl(java.lang.String,boolean)
                    .loginProcessingUrl("/authentication/login/process"))
            .csrf(AbstractHttpConfigurer::disable) // TODO csrf propre
            .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  InMemoryUserDetailsManager userDetailsService() {
    UserDetails admin = User.withUsername("admin")
            .passwordEncoder(passwordEncoder()::encode)
            .password("password")
            .roles("ADMIN")
            .build();
    UserDetails user = User.withUsername("user")
            .passwordEncoder(passwordEncoder()::encode)
            .password("password")
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(admin, user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
