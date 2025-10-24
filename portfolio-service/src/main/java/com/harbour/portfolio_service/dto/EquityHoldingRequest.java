package com.harbour.portfolio_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquityHoldingRequest
{
   @NotBlank(message = "Stock symbol is required")
   @Size(min = 1, max = 10, message = "Symbol must be between 1 and 10 characters")
   private String stockSymbol;

   @NotNull(message = "Quantity is required")
   @Min(value = 1, message = "Quantity must be at least 1")
   private Integer quantity;

   @NotNull(message = "Average price is required")
   @Positive(message = "Average price must be positive")
   private Double averagePrice;

   private LocalDate purchaseDate;
}
