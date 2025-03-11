package com.practice.Portfolio.service;

import com.practice.Portfolio.dtoRequest.InvestmentRequestDto;
import com.practice.Portfolio.dtoRequest.PortfolioRequestDto;
import com.practice.Portfolio.dtoResponse.PortfolioListResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioPageResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioValueResponseDto;
import com.practice.Portfolio.model.PortfolioModel;
import jakarta.validation.Valid;

public interface PortfolioService {
    PortfolioPageResponseDto findAllPortfolio(int page, int size);

    PortfolioResponseDto findPortfolioById(Long id);

    PortfolioResponseDto savePortfolio(@Valid PortfolioRequestDto portfolioRequest);

    PortfolioResponseDto updatePortfolio(Long id, @Valid PortfolioRequestDto portfolioRequest);

    void deletePortfolio(Long id);

    PortfolioValueResponseDto calculateTotalValue(Long userId);

    PortfolioListResponseDto findPortfoliosByUserId(Long userId);

    PortfolioResponseDto addOrUpdateInvestment(Long userId, @Valid InvestmentRequestDto investmentRequest);

    void deleteInvestment(Long userId, Long instrumentId);
}
