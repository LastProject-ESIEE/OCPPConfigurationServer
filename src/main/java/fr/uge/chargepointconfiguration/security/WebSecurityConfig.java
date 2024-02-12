//package fr.uge.chargepointconfiguration.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//  /*
//  @Bean
//  public UserDetailsService userDetailsService() {
//    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//    manager.createUser(User.withUsername("user")
//      .password(passwordEncoder().encode("password"))
//      .roles("USER")
//      .build());
//    manager.createUser(User.withUsername("admin")
//      .password(passwordEncoder().encode("admin"))
//      .roles("USER", "ADMIN")
//      .build());
//    return manager;
//  }*/
//
//  @Bean
//  DataSource dataSource() {
//    return new EmbeddedDatabaseBuilder()
//      .generateUniqueName(true)
//      .setScriptEncoding("UTF-8")
//      .addScript("schema.sql")
//      .addScripts("user_data.sql", "country_data.sql")
//      .build();
//  }
//
//  @Bean
//  UserDetailsManager users(DataSource dataSource) {
//    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//    return users;
//  }
//
//  @Bean
//  public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/login")
//        .permitAll()
//        .requestMatchers("/**")
//        .authenticated())
//      .formLogin(Customizer.withDefaults())
//      .httpBasic(Customizer.withDefaults())
//      .headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable));
//    return http.build();
//  }
//
//  @Bean
//  SecurityFilterChain web (HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/index")
//      .hasAnyAuthority("USER", "ADMIN")
//      .anyRequest()
//      .authenticated());
//    return http.build();
//  }
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
//
//  public static void main(String[] args) {
//    var need = new WebSecurityConfig();
//    System.out.println(need.passwordEncoder().encode("admin"));
//    System.out.println(need.passwordEncoder().encode("axel"));
//    System.out.println(need.passwordEncoder().encode("maxime"));
//    System.out.println(need.passwordEncoder().encode("marius"));
//    System.out.println(need.passwordEncoder().encode("gael"));
//  }
//}
