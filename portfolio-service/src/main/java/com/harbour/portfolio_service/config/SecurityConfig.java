package com.harbour.portfolio_service.config;

import com.harbour.portfolio_service.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig
{
   private final JwtTokenFilter jwtTokenFilter;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
   {
      http.csrf(AbstractHttpConfigurer::disable) // JWT = stateless, no CSRF needed
               .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**")
                        .permitAll() // only Auth endpoints public
                        .anyRequest().authenticated() // everything else needs JWT
               ).addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception
   {
      return authConfig.getAuthenticationManager();
   }
}
