package com.practice.exceptions;

public class InsufficientInstrumentsException extends RuntimeException {
    public InsufficientInstrumentsException(String format) {
        super(format);
    }
}
