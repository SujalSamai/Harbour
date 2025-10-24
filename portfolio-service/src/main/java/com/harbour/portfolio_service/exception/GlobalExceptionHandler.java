package com.harbour.portfolio_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{

   @ExceptionHandler(PortfolioNotFoundException.class)
   public ResponseEntity<String> handlePortfolioNotFound(PortfolioNotFoundException ex)
   {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
   }

   @ExceptionHandler(EquityHoldingNotFoundException.class)
   public ResponseEntity<String> handleHoldingNotFound(EquityHoldingNotFoundException ex)
   {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
   }

   @ExceptionHandler(EquityDataNotFoundException.class)
   public ResponseEntity<String> handleEquityDataNotFound(EquityDataNotFoundException ex)
   {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
   }
}
