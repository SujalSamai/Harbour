package com.harbour.portfolio_service.exception;

public class PortfolioNotFoundException extends RuntimeException
{
   public PortfolioNotFoundException(Long portfolioId)
   {
      super("Portfolio not found with ID: " + portfolioId);
   }
}
