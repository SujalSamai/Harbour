package com.harbour.portfolio_service.controller;

import com.harbour.portfolio_service.dto.EquityHoldingDetails;
import com.harbour.portfolio_service.dto.EquityHoldingRequest;
import com.harbour.portfolio_service.dto.EquityHoldingResponse;
import com.harbour.portfolio_service.service.EquityHoldingsManagerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio/{portfolioId}/holdings")
public class EquityHoldingsController
{
   private final EquityHoldingsManagerService equityHoldingsManagerService;

   public EquityHoldingsController(EquityHoldingsManagerService equityHoldingsManagerService)
   {
      this.equityHoldingsManagerService = equityHoldingsManagerService;
   }


   // POST /api/portfolio/{portfolioId}/holdings
   @PostMapping
   public ResponseEntity<EquityHoldingResponse> addHolding(@PathVariable Long portfolioId,
            @AuthenticationPrincipal Long userId, @Valid @RequestBody EquityHoldingRequest request)
   {

      EquityHoldingResponse response =
               equityHoldingsManagerService.addHoldingToPortfolio(portfolioId, userId, request);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   // GET /api/portfolio/{portfolioId}/holdings
   @GetMapping
   public ResponseEntity<List<EquityHoldingDetails>> getAllHoldings(@PathVariable Long portfolioId,
            @AuthenticationPrincipal Long userId)
   {

      List<EquityHoldingDetails> holdings =
               equityHoldingsManagerService.getHoldingsByPortfolioId(portfolioId, userId);
      return ResponseEntity.ok(holdings);
   }

   // GET /api/portfolio/{portfolioId}/holdings/{holdingId}
   @GetMapping("/{holdingId}")
   public ResponseEntity<EquityHoldingDetails> getHoldingById(@PathVariable Long portfolioId,
            @PathVariable Long holdingId, @AuthenticationPrincipal Long userId)
   {

      EquityHoldingDetails holding =
               equityHoldingsManagerService.getHoldingById(portfolioId, holdingId, userId);
      return ResponseEntity.ok(holding);
   }

   // PUT /api/portfolio/{portfolioId}/holdings/{holdingId}
   @PutMapping("/{holdingId}")
   public ResponseEntity<EquityHoldingResponse> updateHolding(@PathVariable Long portfolioId,
            @PathVariable Long holdingId, @AuthenticationPrincipal Long userId,
            @Valid @RequestBody EquityHoldingRequest request)
   {

      EquityHoldingResponse response =
               equityHoldingsManagerService.updateHolding(portfolioId, holdingId, userId, request);
      return ResponseEntity.ok(response);
   }

   // DELETE /api/portfolio/{portfolioId}/holdings/{holdingId}
   @DeleteMapping("/{holdingId}")
   public ResponseEntity<Void> deleteHolding(@PathVariable Long portfolioId,
            @PathVariable Long holdingId, @AuthenticationPrincipal Long userId)
   {

      equityHoldingsManagerService.deleteHolding(portfolioId, holdingId, userId);

      return ResponseEntity.noContent().build();
   }
}
