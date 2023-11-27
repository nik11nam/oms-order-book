package com.assessment.orderbook.repository;

import com.assessment.orderbook.entity.Execution;

import java.util.List;

public interface ExecutionRepository {
    Long save(Execution execution);

    int getCount();

    void deleteAll();
}
