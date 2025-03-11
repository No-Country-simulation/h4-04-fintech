package com.practice.exceptions;

public class PortfolioHasTransactionsException extends RuntimeException {
    public PortfolioHasTransactionsException(String format) {
        super(format);
    }
}
