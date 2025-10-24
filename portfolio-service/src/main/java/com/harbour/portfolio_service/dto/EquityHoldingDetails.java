package com.harbour.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquityHoldingDetails
{
   private EquityHoldingResponse equityPurchaseData;
   private EquityMetricsDetails equityCurrentMetrics;
}
