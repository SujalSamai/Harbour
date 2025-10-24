package com.harbour.auth_service.entity;

import jakarta.persistence.*;

/**
 * JPA Entity representing a User in the authentication service.
 * The 'id' (userId) is the principal stored in the JWT.
 */
@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true)
   private String username;

   @Column(nullable = false)
   private String password; // Stores the BCrypt hashed password

   public User() {}

   public User(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
