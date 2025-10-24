package com.harbour.auth_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class responsible for generating JWT tokens upon successful authentication. It uses the
 * user's Long ID as the JWT subject.
 */
@Component
public class JwtTokenProvider
{

   private final Key secretKey;

   // Token validity in milliseconds (1 hour)
   private static final long JWT_EXPIRATION = 3600000;


   public JwtTokenProvider(@Value("${jwt.secret}") String secret)
   {
      byte[] keyBytes = Base64.getDecoder().decode(secret);
      this.secretKey = Keys.hmacShaKeyFor(keyBytes);
   }

   /**
    * Generates a JWT token containing the user's ID as the subject claim.
    *
    * @param authentication The Spring Security Authentication object, where the principal is the
    *                       Long user ID.
    * @return The generated JWT token string.
    */
   public String generateToken(Authentication authentication)
   {
      // The principal is the Long userId fetched from the AuthService
      Long userId = (Long) authentication.getPrincipal();
      Date currentDate = new Date();
      Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

      String token = Jwts.builder().setSubject(
                        Long.toString(userId)) // CRUCIAL: Embed the Long user ID as the token subject
               .setIssuedAt(new Date()).setExpiration(expireDate)
               .signWith(secretKey, SignatureAlgorithm.HS512).compact();

      return token;
   }
}

