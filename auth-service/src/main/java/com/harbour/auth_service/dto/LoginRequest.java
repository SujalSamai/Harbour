package com.harbour.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for carrying login/registration credentials from the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest
{

   @NotBlank(message = "Username is required")
   private String username;

   @NotBlank(message = "Password is required")
   private String password;
}
