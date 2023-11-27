package com.assessment.orderbook.repository;

import com.assessment.orderbook.entity.Execution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.assessment.orderbook.MockTestUtils.mockExecution;
import static com.assessment.orderbook.MockTestUtils.mockInstrument;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ExecutionRepositoryTest {

    @Autowired
    private ExecutionRepository executionRepository;

    @AfterEach
    public void destroy() {
        executionRepository.deleteAll();
    }

    @Test
    public void testSave() {
        Execution execution = mockExecution(mockInstrument(1L));

        Long id = executionRepository.save(execution);
        assertThat(id).isEqualTo(1);
    }

    @Test
    public void testCount() {
        Execution execution = mockExecution(mockInstrument(1L));
        executionRepository.save(execution);

        assertThat(executionRepository.getCount()).isEqualTo(1);
    }
}
