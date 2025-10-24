package com.harbour.portfolio_service.repository;

import com.harbour.portfolio_service.entity.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<UserPortfolio, Long>
{
   List<UserPortfolio> findAllByUserId(Long userId);
}
