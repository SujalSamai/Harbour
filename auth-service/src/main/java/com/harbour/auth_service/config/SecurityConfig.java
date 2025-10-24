package com.harbour.auth_service.config;

import com.harbour.auth_service.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the Authentication Service.
 * Only the /api/auth/** endpoints are exposed publicly.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final AuthService authService;

   public SecurityConfig(AuthService authService) {
      this.authService = authService;
   }

   @Bean
   public static PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
   }

   /**
    * Exposes the AuthenticationManager bean, which is needed by the AuthController for login.
    */
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
      return configuration.getAuthenticationManager();
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
               .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless REST APIs
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth -> auth
                        // Expose register and login endpoints publicly
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated() // All other endpoints are secured
               );

      return http.build();
   }
}
