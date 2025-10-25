package com.harbour.portfolio_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Custom JWT filter to process a JWT token in the Authorization header,
 * validate it, and set the user's ID as the principal in the SecurityContext.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter
{

   private final JwtTokenProvider tokenProvider;

   public JwtTokenFilter(JwtTokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
   {

      try {
         // 1. Extract the JWT from the request header
         String jwt = getJwtFromRequest(request);

         // 2. Validate the token and check for an existing authentication
         if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 3. Extract the Long userId (the principal) from the token
            Long userId = tokenProvider.getUserIdFromJWT(jwt);

            // 4. Create an Authentication token for the userId
            // We use null for credentials and GrantedAuthorities because authentication is already done by the JWT
            UsernamePasswordAuthenticationToken
                     authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

            // 5. Add request details
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. Set the Authentication object in the Security Context
            // This makes the userId available via @AuthenticationPrincipal
            SecurityContextHolder.getContext().setAuthentication(authentication);
         }
      } catch (Exception ex) {
         // Log the exception (e.g., malformed token, validation error) and continue the filter chain
         logger.error("Could not set user authentication in security context", ex);
      }

      // Continue the filter chain
      filterChain.doFilter(request, response);
   }

   /**
    * Extracts the JWT token from the "Authorization: Bearer <token>" header.
    */
   private String getJwtFromRequest(HttpServletRequest request) {
      String bearerToken = request.getHeader("Authorization");

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;
   }
}
