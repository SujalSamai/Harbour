package com.harbour.portfolio_service.controller;

import com.harbour.portfolio_service.dto.PortfolioRequest;
import com.harbour.portfolio_service.dto.PortfolioResponse;
import com.harbour.portfolio_service.service.PortfolioManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController
{
   private final PortfolioManagerService portfolioManagerService;

   @Autowired
   public PortfolioController(final PortfolioManagerService portfolioManagerService)
   {
      this.portfolioManagerService = portfolioManagerService;
   }


   @PostMapping
   public ResponseEntity<PortfolioResponse> createPortfolio(@AuthenticationPrincipal Long userId,
            @Valid @RequestBody PortfolioRequest request)
   {

      PortfolioResponse response = portfolioManagerService.createPortfolio(userId, request);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   @GetMapping
   public ResponseEntity<List<PortfolioResponse>> getAllPortfolios(
            @AuthenticationPrincipal Long userId)
   {
      List<PortfolioResponse> portfolios = portfolioManagerService.getAllPortfolios(userId);
      return ResponseEntity.ok(portfolios);
   }

   @GetMapping("/{id}")
   public ResponseEntity<PortfolioResponse> getPortfolioById(@AuthenticationPrincipal Long userId,
            @PathVariable("id") Long portfolioId)
   {
      PortfolioResponse portfolio = portfolioManagerService.getPortfolioById(portfolioId, userId);
      return ResponseEntity.ok(portfolio);
   }

   @PutMapping("/{id}")
   public ResponseEntity<PortfolioResponse> updatePortfolio(@PathVariable("id") Long portfolioId,
            @AuthenticationPrincipal Long userId, @Valid @RequestBody PortfolioRequest request)
   {

      // Authorization and logic handled in the service layer
      PortfolioResponse response =
               portfolioManagerService.updatePortfolio(portfolioId, userId, request);

      return ResponseEntity.ok(response);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deletePortfolio(@PathVariable("id") Long portfolioId,
            @AuthenticationPrincipal Long userId)
   {

      portfolioManagerService.deletePortfolio(portfolioId, userId);

      return ResponseEntity.noContent().build();
   }
}
