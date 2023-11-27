package com.assessment.orderbook.repository.impl;

import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.repository.ExecutionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ExecutionInMemoryRepository implements ExecutionRepository {

    private final List<Execution> executions = new ArrayList<>();

    @Override
    public Long save(Execution execution) {
        // Mimic setting id for execution during persistence
        Long id = Long.valueOf(executions.size() > 0 ? executions.size()+1 : 1);
        execution.setId(id);

        executions.add(execution);
        return id;
    }

    @Override
    public int getCount() {
        return executions.size();
    }

    @Override
    public void deleteAll() {
        executions.clear();
    }
}
