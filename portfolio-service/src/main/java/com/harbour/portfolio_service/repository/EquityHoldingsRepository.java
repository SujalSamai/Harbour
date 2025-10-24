package com.harbour.portfolio_service.repository;

import com.harbour.portfolio_service.entity.EquityHoldings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquityHoldingsRepository extends JpaRepository<EquityHoldings, Long>
{
   // Find all holdings belonging to a specific portfolio ID
   List<EquityHoldings> findAllByPortfolioId(Long portfolioId);

   // Find a specific holding by its ID AND confirm it belongs to the given portfolio ID.
   Optional<EquityHoldings> findByIdAndPortfolioId(Long id, Long portfolioId);
}
