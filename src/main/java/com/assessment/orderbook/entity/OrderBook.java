package com.assessment.orderbook.entity;

import com.assessment.orderbook.constants.OrderBookStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
public final class OrderBook {
    private final Long id; // will be set when persisting
    private final Instrument instrument;
    private final List<Order> orders;
    private final List<Execution> executions;
    @Setter
    private OrderBookStatus status;

    public OrderBook(Long id, Instrument instrument) {
        this.id = id;
        this.instrument = instrument;
        orders = new ArrayList<>();
        executions = new ArrayList<>();
        status = OrderBookStatus.OPEN;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addExecution(Execution execution) {
        executions.add(execution);
    }

}
