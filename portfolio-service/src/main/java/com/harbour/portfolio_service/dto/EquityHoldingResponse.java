package com.harbour.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquityHoldingResponse
{
   private Long id;
   private Long portfolioId;
   private String stockSymbol;
   private Integer quantity;
   private Double averagePrice;
   private LocalDate purchaseDate;
   private LocalDateTime createdAt;

}
