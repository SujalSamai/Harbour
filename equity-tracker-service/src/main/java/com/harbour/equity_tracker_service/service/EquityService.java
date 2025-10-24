package com.harbour.equity_tracker_service.service;

import com.harbour.equity_tracker_service.client.WebScraperClient;
import com.harbour.equity_tracker_service.dto.EquityDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EquityService
{
   private final WebScraperClient webScraperClient;

   @Autowired
   public EquityService(final WebScraperClient webScraperClient)
   {
      this.webScraperClient = webScraperClient;
   }

   @Cacheable(value = "stocks", key = "#stockSymbol")
   public EquityDetailsResponse getStockForSymbol(final String stockSymbol)
   {
      return webScraperClient.getStockData(stockSymbol);
   }
}
