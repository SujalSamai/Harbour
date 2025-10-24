package com.harbour.auth_service.service;

import com.harbour.auth_service.entity.User;
import com.harbour.auth_service.repository.UserRepository;
import com.harbour.auth_service.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles user registration logic and acts as the UserDetailsService for Spring Security.
 */
@Service
public class AuthService implements UserDetailsService
{

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

   public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder)
   {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }

   /**
    * Implementation of UserDetailsService. Used by AuthenticationManager during login to fetch user
    * credentials. * @param username The username provided during login.
    *
    * @return A CustomUserDetails object wrapping the User entity.
    * @throws UsernameNotFoundException if the user does not exist.
    */
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
   {
      // Retrieve the User entity from the database
      User user = userRepository.findByUsername(username).orElseThrow(
               () -> new UsernameNotFoundException("User not found with username: " + username));

      // Wrap the User entity in our custom implementation
      // This ensures the Long ID is set as the authentication principal.
      return new CustomUserDetails(user);
   }

   /**
    * Registers a new user account. * @param username The username for the new user.
    *
    * @param password The raw password.
    * @return The newly created User entity.
    * @throws RuntimeException if the username already exists.
    */
   public User registerUser(String username, String password)
   {
      if (userRepository.existsByUsername(username))
      {
         throw new RuntimeException("Username already taken.");
      }

      User user = new User();
      user.setUsername(username);
      // Hash and set the password using BCrypt
      user.setPassword(passwordEncoder.encode(password));

      return userRepository.save(user);
   }
}
