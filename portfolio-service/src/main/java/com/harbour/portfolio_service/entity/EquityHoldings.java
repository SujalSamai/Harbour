package com.harbour.portfolio_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "equity_holdings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquityHoldings
{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // Foreign Key to the Portfolio entity
   // This is how we ensure a holding belongs to a specific portfolio
   private Long portfolioId;

   private String stockSymbol; // e.g., "TCS", "BSE"

   private int quantity; // Number of shares

   private double averagePrice; // Price paid per share

   private LocalDate purchaseDate;

   private LocalDateTime createdAt;

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
   }
}
