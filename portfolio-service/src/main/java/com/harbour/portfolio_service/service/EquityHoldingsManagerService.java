package com.harbour.portfolio_service.service;

import com.harbour.portfolio_service.client.ScreenerWebScrapper;
import com.harbour.portfolio_service.dto.EquityHoldingDetails;
import com.harbour.portfolio_service.dto.EquityHoldingRequest;
import com.harbour.portfolio_service.dto.EquityHoldingResponse;
import com.harbour.portfolio_service.entity.EquityHoldings;
import com.harbour.portfolio_service.entity.UserPortfolio;
import com.harbour.portfolio_service.exception.EquityDataNotFoundException;
import com.harbour.portfolio_service.exception.EquityHoldingNotFoundException;
import com.harbour.portfolio_service.exception.PortfolioNotFoundException;
import com.harbour.portfolio_service.repository.EquityHoldingsRepository;
import com.harbour.portfolio_service.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EquityHoldingsManagerService
{
   private final PortfolioRepository portfolioRepository;
   private final EquityHoldingsRepository equityHoldingsRepository;
   private final ScreenerWebScrapper screenerWebScrapper;

   @Autowired
   public EquityHoldingsManagerService(final PortfolioRepository portfolioRepository,
            final EquityHoldingsRepository equityHoldingsRepository,
            final ScreenerWebScrapper screenerWebScrapper)
   {
      this.portfolioRepository = portfolioRepository;
      this.equityHoldingsRepository = equityHoldingsRepository;
      this.screenerWebScrapper = screenerWebScrapper;
   }

   public EquityHoldingResponse addHoldingToPortfolio(Long portfolioId, Long userId,
            EquityHoldingRequest request)
   {
      // Authorization: Check if the user owns the portfolio
      getPortfolioIfOwned(portfolioId, userId);

      // Conversion and Assignment
      EquityHoldings holding = mapToHoldingEntity(request);
      holding.setPortfolioId(portfolioId);

      EquityHoldings savedHolding = equityHoldingsRepository.save(holding);
      return mapToHoldingResponse(savedHolding);
   }

   public List<EquityHoldingDetails> getHoldingsByPortfolioId(Long portfolioId, Long userId)
   {
      getPortfolioIfOwned(portfolioId, userId);
      List<EquityHoldings> holdings = equityHoldingsRepository.findAllByPortfolioId(portfolioId);
      return holdings.stream().map(this::mapToHoldingDetailsResponse).collect(Collectors.toList());
   }

   public EquityHoldingDetails getHoldingById(Long portfolioId, Long holdingId, Long userId)
   {
      getPortfolioIfOwned(portfolioId, userId);

      EquityHoldings holding =
               equityHoldingsRepository.findByIdAndPortfolioId(holdingId, portfolioId)
                        .orElseThrow(() -> new EquityHoldingNotFoundException(holdingId));

      return mapToHoldingDetailsResponse(holding);
   }

   public EquityHoldingResponse updateHolding(Long portfolioId, Long holdingId, Long userId,
            EquityHoldingRequest request)
   {
      getPortfolioIfOwned(portfolioId, userId);

      EquityHoldings holding =
               equityHoldingsRepository.findByIdAndPortfolioId(holdingId, portfolioId)
                        .orElseThrow(() -> new EquityHoldingNotFoundException(holdingId));

      // Apply Updates
      holding.setStockSymbol(request.getStockSymbol());
      holding.setQuantity(request.getQuantity());
      holding.setAveragePrice(request.getAveragePrice());
      if (request.getPurchaseDate() != null)
      {
         holding.setPurchaseDate(request.getPurchaseDate());
      }

      EquityHoldings updatedHolding = equityHoldingsRepository.save(holding);
      return mapToHoldingResponse(updatedHolding);
   }

   public void deleteHolding(Long portfolioId, Long holdingId, Long userId)
   {
      getPortfolioIfOwned(portfolioId, userId);

      EquityHoldings holding =
               equityHoldingsRepository.findByIdAndPortfolioId(holdingId, portfolioId)
                        .orElseThrow(() -> new EquityHoldingNotFoundException(holdingId));

      equityHoldingsRepository.delete(holding);
   }


   private UserPortfolio getPortfolioIfOwned(Long portfolioId, Long userId)
   {
      UserPortfolio portfolio = portfolioRepository.findById(portfolioId)
               .orElseThrow(() -> new PortfolioNotFoundException(portfolioId));

      // Ensure the portfolio belongs to the authenticated user
      if (!portfolio.getUserId().equals(userId))
      {
         throw new PortfolioNotFoundException(portfolioId);
      }
      return portfolio;
   }

   private EquityHoldings mapToHoldingEntity(EquityHoldingRequest request)
   {
      EquityHoldings holding = new EquityHoldings();
      holding.setStockSymbol(request.getStockSymbol());
      holding.setQuantity(request.getQuantity());
      holding.setAveragePrice(request.getAveragePrice());
      holding.setPurchaseDate(request.getPurchaseDate());
      return holding;
   }

   private EquityHoldingResponse mapToHoldingResponse(EquityHoldings holding)
   {
      EquityHoldingResponse response = new EquityHoldingResponse();
      response.setId(holding.getId());
      response.setPortfolioId(holding.getPortfolioId());
      response.setStockSymbol(holding.getStockSymbol());
      response.setQuantity(holding.getQuantity());
      response.setAveragePrice(holding.getAveragePrice());
      response.setPurchaseDate(holding.getPurchaseDate());
      response.setCreatedAt(holding.getCreatedAt());
      return response;
   }

   private EquityHoldingDetails mapToHoldingDetailsResponse(EquityHoldings holding)
   {
      String url = "https://www.screener.in/company/" + holding.getStockSymbol() + "/consolidated/";
      EquityHoldingDetails holdingDetails = screenerWebScrapper.scrapDataFromScreener(holding.getStockSymbol(), url);

      if (Objects.isNull(holdingDetails))
      {
         throw new EquityDataNotFoundException(holding.getStockSymbol());
      }

      return holdingDetails;
   }
}
