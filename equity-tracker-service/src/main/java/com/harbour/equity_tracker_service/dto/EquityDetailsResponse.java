package com.harbour.equity_tracker_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquityDetailsResponse
{
   private String symbol;
   private String companyName;
   private String price;
   private String changeInPercentage;
   private String description;
   private String businessOverview;
   private String stockPERatio;
   private String dividendYield;
   private String bookValue;
   private String roce;
   private String roe;
   private String highValue;
   private String lowValue;
   private String marketCap;
   private String faceValue;
}
