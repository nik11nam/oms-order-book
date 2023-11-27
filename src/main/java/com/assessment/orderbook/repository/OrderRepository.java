package com.assessment.orderbook.repository;

import com.assessment.orderbook.entity.Order;

import java.util.List;

public interface OrderRepository {
    Long save(Order order);

    int getCount();

    void deleteAll();
}
