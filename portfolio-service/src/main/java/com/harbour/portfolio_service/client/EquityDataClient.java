package com.harbour.portfolio_service.client;

import com.harbour.portfolio_service.dto.EquityMetricsDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EquityDataClient
{
   @Value("${equity.tracking.service.url:http://localhost:4001}")
   private String equityTrackingServiceUrl;

   private final RestTemplate restTemplate;

   public EquityDataClient(RestTemplate restTemplate)
   {
      this.restTemplate = restTemplate;
   }

   public EquityMetricsDetails getDataFromEquityTrackerService(String stockSymbol)
   {
      String url =
               UriComponentsBuilder.fromHttpUrl(equityTrackingServiceUrl).path("/api/stocks/{symbol}")
                        .buildAndExpand(stockSymbol).toUriString();

      try
      {
         return restTemplate.getForObject(url, EquityMetricsDetails.class);
      }
      catch (HttpClientErrorException.NotFound e)
      {
         System.err.println("Metrics not found for symbol: " + stockSymbol);
         return null;
      }
      catch (Exception e)
      {
         System.err.println("Error calling Equity Tracker Data Service: " + e.getMessage());
         return null;
      }
   }
}
