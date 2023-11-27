package com.assessment.orderbook.repository.impl;

import com.assessment.orderbook.entity.Instrument;
import com.assessment.orderbook.repository.InstrumentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InstrumentInMemoryRepository implements InstrumentRepository {

    // Collection of instruments available in system
    private final Map<Long, Instrument> instrumentMap = new HashMap<>();
    @Value("#{${order-book.instrument.id}}")
    private List<Long> instrumentIds;

    @PostConstruct
    public void init() {
        for (Long instrumentId : instrumentIds) {
            Instrument instrument = new Instrument(instrumentId, "Instrument-" + instrumentId);
            instrumentMap.put(instrumentId, instrument);
        }
    }

    @Override
    public Optional<Instrument> getById(Long id) {
        return Optional.ofNullable(instrumentMap.get(id));
    }

    @Override
    public List<Instrument> getAll() {
        return instrumentMap.values().stream().toList();
    }
}
