package com.practice.exceptions;

public class InstrumentNotFoundException extends RuntimeException {
    public InstrumentNotFoundException(String format) {
        super(format);
    }
}
