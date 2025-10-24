package com.harbour.portfolio_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_portfolio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPortfolio
{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private Long userId;

   private String name;

   private LocalDateTime createdAt;

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
   }

   public UserPortfolio(Long userId, String name) {
      this.userId = userId;
      this.name = name;
   }
}
