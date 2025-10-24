package com.harbour.portfolio_service.service;

import com.harbour.portfolio_service.dto.PortfolioRequest;
import com.harbour.portfolio_service.dto.PortfolioResponse;
import com.harbour.portfolio_service.entity.UserPortfolio;
import com.harbour.portfolio_service.exception.PortfolioNotFoundException;
import com.harbour.portfolio_service.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioManagerService
{
   private final PortfolioRepository portfolioRepository;

   public PortfolioManagerService(PortfolioRepository portfolioRepository)
   {
      this.portfolioRepository = portfolioRepository;
   }

   public PortfolioResponse createPortfolio(Long userId, PortfolioRequest request)
   {
      UserPortfolio portfolio = new UserPortfolio(userId, request.getPortfolioName());
      UserPortfolio savedPortfolio = portfolioRepository.save(portfolio);
      return mapToPortfolioResponse(savedPortfolio);
   }

   public List<PortfolioResponse> getAllPortfolios(Long userId)
   {
      // Retrieve all portfolios associated with the given userId
      List<UserPortfolio> portfolios = portfolioRepository.findAllByUserId(userId);
      return portfolios.stream().map(this::mapToPortfolioResponse).collect(Collectors.toList());
   }

   public PortfolioResponse getPortfolioById(Long portfolioId, Long userId)
   {
      Optional<UserPortfolio> portfolioOptional = portfolioRepository.findById(portfolioId);
      if (portfolioOptional.isEmpty())
      {
         throw new PortfolioNotFoundException(portfolioId);
      }

      UserPortfolio portfolio = portfolioOptional.get();

      // Authorization Check (Crucial Step)
      // Ensure the portfolio belongs to the authenticated user
      if (!portfolio.getUserId().equals(userId))
      {
         throw new PortfolioNotFoundException(portfolioId);
      }
      return mapToPortfolioResponse(portfolio);
   }

   public PortfolioResponse updatePortfolio(Long portfolioId, Long userId, PortfolioRequest request)
   {
      UserPortfolio portfolio = portfolioRepository.findById(portfolioId)
               .orElseThrow(() -> new PortfolioNotFoundException(portfolioId));
      if (!portfolio.getUserId().equals(userId))
      {
         throw new PortfolioNotFoundException(portfolioId); // Deny access
      }

      if (request.getPortfolioName() != null && !request.getPortfolioName().isBlank())
      {
         portfolio.setName(request.getPortfolioName());
      }

      UserPortfolio updatedPortfolio = portfolioRepository.save(portfolio);
      return mapToPortfolioResponse(updatedPortfolio);
   }

   public void deletePortfolio(Long portfolioId, Long userId) {
      UserPortfolio portfolio = portfolioRepository.findById(portfolioId)
               .orElseThrow(() -> new PortfolioNotFoundException(portfolioId));
      if (!portfolio.getUserId().equals(userId)) {
         throw new PortfolioNotFoundException(portfolioId);
      }

      // NOTE: Ensure your database schema (JPA entities/tables) is configured
      // to CASCADE the deletion to associated HOLDING and ALERT records.
      portfolioRepository.delete(portfolio);
   }

   // Helper method for mapping (to keep logic clean)
   private PortfolioResponse mapToPortfolioResponse(UserPortfolio portfolio)
   {
      return PortfolioResponse.builder().id(portfolio.getId()).name(portfolio.getName())
               .createdAt(portfolio.getCreatedAt()).build();
   }
}
