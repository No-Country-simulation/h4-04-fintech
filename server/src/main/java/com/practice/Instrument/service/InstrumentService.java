package com.practice.Instrument.service;

import com.practice.Instrument.dtoResponse.InstrumentPageResponseDto;

public interface InstrumentService {
    InstrumentPageResponseDto findAllInstrument(int page, int size);

    double getCurrentPrice(Long id);
}
