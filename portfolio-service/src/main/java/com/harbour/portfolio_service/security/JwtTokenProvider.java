package com.harbour.portfolio_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

/**
 * Utility class for the Portfolio Service to validate and parse JWTs issued by the Auth Service.
 * NOTE: The secret key must match the one used in the Auth Service.
 */
@Component
public class JwtTokenProvider
{

   @Value("${jwt.secret}")
   private String jwtSecret;

   private Key getSigningKey()
   {
      // Use the same signing mechanism as the Auth Service (Keys.hmacShaKeyFor)
      return Keys.hmacShaKeyFor(jwtSecret.getBytes());
   }

   /**
    * Extracts the userId (the subject of the token) from the JWT.
    *
    * @param token The JWT string.
    * @return The userId (Long) embedded as the subject.
    */
   public Long getUserIdFromJWT(String token)
   {
      Claims claims = Jwts.parser()
               .verifyWith((SecretKey) getSigningKey())
               .build().parseSignedClaims(token).getPayload();

      // The Auth Service sets the Long userId as the subject
      return Long.parseLong(claims.getSubject());
   }

   /**
    * Validates the integrity and expiration of the JWT.
    *
    * @param authToken The JWT string.
    * @return true if the token is valid, false otherwise.
    */
   public boolean validateToken(String authToken)
   {
      try
      {
         Jwts.parser().verifyWith((SecretKey) getSigningKey()).build().parseSignedClaims(authToken);
         return true;

      }
      catch (Exception ex)
      {
         // JWT claims string is empty
         // Log this error
      }
      return false;
   }
}

