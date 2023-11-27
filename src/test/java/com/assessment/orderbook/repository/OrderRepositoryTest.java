package com.assessment.orderbook.repository;

import com.assessment.orderbook.constants.OrderType;
import com.assessment.orderbook.entity.Instrument;
import com.assessment.orderbook.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.assessment.orderbook.MockTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @AfterEach
    public void destroy() {
        orderRepository.deleteAll();
    }

    @Test
    public void testSave() {
         Instrument instrument = mockInstrument(1L);
         Order o1 = mockLimitOrder(instrument);
         Long id = orderRepository.save(o1);
         assertThat(id).isEqualTo(1);

         Order o2 = mockMarketOrder(instrument);
         id = orderRepository.save(o2);
         assertThat(id).isEqualTo(2);
    }

    @Test
    public void testCount() {

        Instrument instrument = mockInstrument(1L);
        Order o1 = mockLimitOrder(instrument);
        Order o2 = mockMarketOrder(instrument);

        orderRepository.save(o1);
        orderRepository.save(o2);

        assertThat(orderRepository.getCount()).isEqualTo(2);

    }

}
