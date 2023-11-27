package com.assessment.orderbook.service.impl;

import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.entity.OrderBook;
import com.assessment.orderbook.repository.ExecutionRepository;
import com.assessment.orderbook.repository.OrderBookRepository;
import com.assessment.orderbook.constants.OrderBookStatus;
import com.assessment.orderbook.exception.ItemNotFoundException;
import com.assessment.orderbook.repository.OrderRepository;
import com.assessment.orderbook.service.OrderBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrderBookServiceImpl implements OrderBookService {
    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ExecutionRepository executionRepository;

    @Override
    public OrderBook getOrderBook(Long instrumentId) {
        return getOrderBookByInstrumentId(instrumentId);
    }

    @Override
    public boolean modifyOrderBookStatus(Long instrumentId, OrderBookStatus status) {
        OrderBook orderBook = getOrderBookByInstrumentId(instrumentId);
        orderBook.setStatus(status);

        log.info("Updating order book status for id: {}", orderBook.getId());
        return orderBookRepository.update(orderBook.getId(), orderBook);
    }

    @Override
    public Order addOrderToBook(Order order) {
        Long id = orderRepository.save(order);
        order.setId(id);

        OrderBook orderBook = getOrderBookByInstrumentId(order.getInstrument().getId());
        orderBook.addOrder(order);

        log.info("Added new order: {} to order book: {}", order.getId(), orderBook.getId());
        orderBookRepository.update(orderBook.getId(), orderBook);
        return order;
    }

    @Override
    public Execution addExecutionToBook(Execution execution) {
        Long id = executionRepository.save(execution);
        execution.setId(id);

        OrderBook orderBook = getOrderBookByInstrumentId(execution.getInstrument().getId());
        orderBook.addExecution(execution);

        log.info("Added new execution: {} to order book: {}", execution.getId(), orderBook.getId());
        orderBookRepository.update(orderBook.getId(), orderBook);
        return execution;
    }

    private OrderBook getOrderBookByInstrumentId(Long instrumentId) {
        Optional<OrderBook> result = orderBookRepository.findByInstrumentId(instrumentId);
        if(result.isEmpty()) {
            throw new ItemNotFoundException("Order Book not available for instrument id: " + instrumentId);
        }
        return result.get();
    }
}
