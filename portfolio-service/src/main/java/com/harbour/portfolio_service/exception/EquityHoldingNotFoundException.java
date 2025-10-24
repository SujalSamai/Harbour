package com.harbour.portfolio_service.exception;

public class EquityHoldingNotFoundException extends RuntimeException
{
   public EquityHoldingNotFoundException(Long holdingId)
   {
      super("Holding not found with ID: " + holdingId);
   }
}
