package com.assessment.orderbook.repository;

import com.assessment.orderbook.entity.OrderBook;

import java.util.Optional;

public interface OrderBookRepository {

    Optional<OrderBook> findByInstrumentId(Long instrumentId);

    boolean update(Long id, OrderBook orderBook);

}
