package com.assessment.orderbook.repository;

import com.assessment.orderbook.entity.Instrument;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {
    Optional<Instrument> getById(Long id);

    List<Instrument> getAll();
}
