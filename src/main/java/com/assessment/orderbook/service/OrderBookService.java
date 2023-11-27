package com.assessment.orderbook.service;

import com.assessment.orderbook.constants.OrderBookStatus;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.entity.OrderBook;

public interface OrderBookService {

    OrderBook getOrderBook(Long instrumentId);

    boolean modifyOrderBookStatus(Long instrumentId, OrderBookStatus status);

    Order addOrderToBook(Order order);

    Execution addExecutionToBook(Execution execution);

}
