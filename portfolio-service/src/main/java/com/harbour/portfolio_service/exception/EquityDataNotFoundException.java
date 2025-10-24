package com.harbour.portfolio_service.exception;

public class EquityDataNotFoundException extends RuntimeException
{
   public EquityDataNotFoundException(String stockSymbol)
   {
      super("No data found for stock symbol: " + stockSymbol);
   }
}
