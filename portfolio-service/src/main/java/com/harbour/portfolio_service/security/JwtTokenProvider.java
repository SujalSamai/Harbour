package com.harbour.portfolio_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

/**
 * Utility class for the Portfolio Service to validate and parse JWTs issued by the Auth Service.
 * NOTE: The secret key must match the one used in the Auth Service.
 */
@Component
public class JwtTokenProvider
{

   private final Key secretKey;

   public JwtTokenProvider(@Value("${jwt.secret}") String secret)
   {
      byte[] keyBytes = Base64.getDecoder().decode(secret);
      this.secretKey = Keys.hmacShaKeyFor(keyBytes);
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
               .verifyWith((SecretKey) secretKey)
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
         Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(authToken);
         System.out.println("Token validation successful");
         return true;

      }
      catch (Exception ex)
      {
         System.err.println("Token validation failed "+ ex);
      }
      return false;
   }
}

