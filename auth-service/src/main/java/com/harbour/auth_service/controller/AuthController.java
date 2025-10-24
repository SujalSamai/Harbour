package com.harbour.auth_service.controller;

import com.harbour.auth_service.dto.JwtResponse;
import com.harbour.auth_service.dto.LoginRequest;
import com.harbour.auth_service.security.JwtTokenProvider;
import com.harbour.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user authentication and authorization endpoints (Register and Login).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController
{

   private final AuthenticationManager authenticationManager;
   private final AuthService authService;
   private final JwtTokenProvider tokenProvider;

   public AuthController(AuthenticationManager authenticationManager, AuthService authService,
            JwtTokenProvider tokenProvider)
   {
      this.authenticationManager = authenticationManager;
      this.authService = authService;
      this.tokenProvider = tokenProvider;
   }

   /**
    * Endpoint for user registration. Path: POST /api/auth/register
    */
   @PostMapping("/register")
   public ResponseEntity<?> registerUser(@Valid @RequestBody LoginRequest request)
   {
      try
      {
         authService.registerUser(request.getUsername(), request.getPassword());
         return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
      }
      catch (RuntimeException e)
      {
         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
   }

   /**
    * Endpoint for user login and JWT generation. Path: POST /api/auth/login
    */
   @PostMapping("/login")
   public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest request)
   {
      // 1. Authenticate the user credentials
      Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));

      // 2. Set authentication in the security context (optional for stateless, but good practice)
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 3. Generate JWT token (contains Long userId as subject)
      String jwt = tokenProvider.generateToken(authentication);

      // 4. Retrieve the authenticated principal (which is the Long userId from CustomUserDetails)
      Long userId = (Long) authentication.getPrincipal();

      // 5. Return the token and user details
      return ResponseEntity.ok(new JwtResponse(jwt, userId, request.getUsername()));
   }
}
