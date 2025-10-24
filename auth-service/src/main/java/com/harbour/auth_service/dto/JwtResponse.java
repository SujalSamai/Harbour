package com.harbour.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse
{
   private String token;
   private String type = "Bearer"; // Standard JWT type identifier
   private Long id; // The Long userId used as the JWT subject/principal
   private String username;

   /**
    * Constructor for a successful authentication response.
    */
   public JwtResponse(String accessToken, Long id, String username)
   {
      this.token = accessToken;
      this.id = id;
      this.username = username;
   }
}

