package fr.uge.chargepointconfiguration.security;

import fr.uge.chargepointconfiguration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
                    authorize.requestMatchers("/api/**").authenticated()
                            .requestMatchers("/login.html").permitAll()
                            .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin.loginPage("/login.html")
                    .failureUrl("/login.html?failed")
                    .defaultSuccessUrl("/youpii")
                    // see : https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configurers/AbstractAuthenticationFilterConfigurer.html#defaultSuccessUrl(java.lang.String,boolean)
                    .loginProcessingUrl("/authentication/login/process"))
            .csrf(AbstractHttpConfigurer::disable) // TODO csrf propre
            .httpBasic(Customizer.withDefaults());
    return http.build();
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
