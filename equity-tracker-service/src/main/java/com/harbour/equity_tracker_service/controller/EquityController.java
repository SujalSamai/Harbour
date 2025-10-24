package com.harbour.equity_tracker_service.controller;

import com.harbour.equity_tracker_service.dto.EquityDetailsResponse;
import com.harbour.equity_tracker_service.service.EquityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class EquityController
{
   private final EquityService equityService;

   @Autowired
   public EquityController(final EquityService equityService)
   {
      this.equityService = equityService;
   }


   @GetMapping("/{stockSymbol}")
   public EquityDetailsResponse getStock(@PathVariable String stockSymbol)
   {
      return equityService.getStockForSymbol(stockSymbol.toUpperCase());
   }
}
