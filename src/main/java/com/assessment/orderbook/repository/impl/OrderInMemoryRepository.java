package com.assessment.orderbook.repository.impl;

import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderInMemoryRepository implements OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public Long save(Order order) {
        // Mimic setting id for order during persistence
        long id = Long.valueOf(orders.size() > 0 ? orders.size()+1: 1);
        order.setId(id);

        orders.add(order);
        return id;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public void deleteAll() {
        orders.clear();
    }
}
