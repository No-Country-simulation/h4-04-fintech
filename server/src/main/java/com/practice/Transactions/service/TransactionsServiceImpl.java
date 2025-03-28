package com.practice.Transactions.service;

import com.practice.Portfolio.model.PortfolioModel;
import com.practice.Portfolio.repository.PortfolioRepository;

import com.practice.Transactions.dtoRequest.TransactionRequestDto;

import com.practice.Transactions.dtoResponse.TransactionPageResponseDto;
import com.practice.Transactions.dtoResponse.TransactionResponseDto;

import com.practice.Transactions.model.TransactionModel;
import com.practice.Transactions.repository.TransactionsRepository;
import com.practice.exceptions.TransactionNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class TransactionsServiceImpl implements TransactionsService {
    private static final String TRANSACTION_NOT_FOUND = "La transacción no existe";
    private static final String PORTFOLIO_NOT_FOUND = "El portafolio no fue encontrado";

    private final TransactionsRepository transactionsRepository;
    private final ModelMapper modelMapper;

    private final PortfolioRepository portfolioRepository;

    @Override
    public TransactionPageResponseDto findAllTransactions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionModel> transactionPage = transactionsRepository.findAll(pageable);

        List<TransactionResponseDto> transactionDtos = transactionPage.getContent()
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionResponseDto.class))
                .collect(Collectors.toList());

        return new TransactionPageResponseDto(transactionDtos, transactionPage.getTotalPages(), transactionPage.getTotalElements());
    }


    @Override
    public TransactionResponseDto getTransactionById(Long id) {
        TransactionModel transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(TRANSACTION_NOT_FOUND));
        return modelMapper.map(transaction, TransactionResponseDto.class);
    }


    @Override
    public TransactionResponseDto saveTransaction(@Valid TransactionRequestDto dto) {
        TransactionModel transaction = modelMapper.map(dto, TransactionModel.class);

        PortfolioModel portfolio = portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new TransactionNotFoundException(PORTFOLIO_NOT_FOUND));
        transaction.setPortfolio(portfolio);

        TransactionModel savedTransaction = transactionsRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionResponseDto.class);
    }


    @Override
    public TransactionResponseDto updateTransaction(Long id, @Valid TransactionRequestDto dto) {
        TransactionModel transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(TRANSACTION_NOT_FOUND));

        modelMapper.map(dto, transaction);

        if (dto.getPortfolioId() != null) {
            PortfolioModel portfolio = portfolioRepository.findById(dto.getPortfolioId())
                    .orElseThrow(() -> new TransactionNotFoundException(PORTFOLIO_NOT_FOUND));
            transaction.setPortfolio(portfolio);
        }

        TransactionModel updatedTransaction = transactionsRepository.save(transaction);
        return modelMapper.map(updatedTransaction, TransactionResponseDto.class);
    }

    @Override
    public void deleteTransaction(Long id) {
        TransactionModel transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(TRANSACTION_NOT_FOUND));
        transactionsRepository.delete(transaction);
    }

}
