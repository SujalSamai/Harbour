package com.harbour.auth_service.security;

import com.harbour.auth_service.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetails to wrap the Auth Service's User entity.
 * This class ensures that the principal returned is the Long userId.
 */
public class CustomUserDetails implements UserDetails
{

   private final User user;

   public CustomUserDetails(User user) {
      this.user = user;
   }

   /**
    * CRUCIAL: Returns the Long User ID, which is used as the principal
    * in the SecurityContext and embedded in the JWT subject.
    */
   public Long getPrincipal() {
      return user.getId();
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      // Simple application, no roles/authorities needed for now
      return Collections.emptyList();
   }

   @Override
   public String getPassword() {
      return user.getPassword();
   }

   @Override
   public String getUsername() {
      return user.getUsername();
   }

   // Standard UserDetails methods (set to true/non-expired)
   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
