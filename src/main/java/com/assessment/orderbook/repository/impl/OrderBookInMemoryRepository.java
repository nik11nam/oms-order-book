package com.assessment.orderbook.repository.impl;

import com.assessment.orderbook.entity.Instrument;
import com.assessment.orderbook.entity.OrderBook;
import com.assessment.orderbook.repository.InstrumentRepository;
import com.assessment.orderbook.repository.OrderBookRepository;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderBookInMemoryRepository implements OrderBookRepository, SmartInitializingSingleton {
    // Order Book id <-> Instrument id, order book per instrument
    private final Map<Long, OrderBook> orderBookMap = new HashMap<>();

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Override
    public void afterSingletonsInstantiated() {
        List<Instrument> instruments = instrumentRepository.getAll();
        for (int i=0; i<instruments.size(); i++) {
            Long id = Long.valueOf(i+1);
            orderBookMap.put(id, new OrderBook(id, instruments.get(i)));
        }
    }

    @Override
    public Optional<OrderBook> findByInstrumentId(Long instrumentId) {
        List<OrderBook> orderBooks = orderBookMap.values().stream().toList();
        return orderBooks.stream()
                .filter(o -> instrumentId.equals(o.getInstrument().getId()))
                .findFirst();
    }
    @Override
    public boolean update(Long id, OrderBook orderBook) {
        orderBookMap.put(id, orderBook);
        return true;
    }
}
