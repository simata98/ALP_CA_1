package com.ktproject.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ktproject.backend.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final AuthenticationConfiguration authenticationConfiguration;

  public SecurityConfig(UserService userService, AuthenticationConfiguration authenticationConfiguration) {
    this.userService = userService;
    this.authenticationConfiguration = authenticationConfiguration;
  }

  @SuppressWarnings({ "deprecation", "removal" })
  @Bean
  SecurityFilterChain servSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors()
        .and()
        .csrf(csrf -> csrf.disable())
        .authorizeRequests(aythorizeRequests -> aythorizeRequests
            .requestMatchers("/auth/register", "/auth/login").permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
            .requestMatchers("assistant").permitAll()
            .requestMatchers("/api/executeQuery", "/api/save-db", "/api/get-urls/**").permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .logoutUrl("/auth/logout")
            .permitAll());
    return http.build();
  }

  @Bean
  CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
    CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
    filter.setAuthenticationManager(AuthenticationManager(authenticationConfiguration));
    return filter;
  }

  @Bean
  UserDetailsService userDetailsService() {
    return username -> userService.findByUsername(username)
        .map(user -> org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager AuthenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
