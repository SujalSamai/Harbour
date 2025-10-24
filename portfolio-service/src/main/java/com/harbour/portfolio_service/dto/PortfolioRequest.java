package com.harbour.portfolio_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRequest
{
   @NotBlank(message = "Portfolio name is required")
   @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
   private String portfolioName;
}
