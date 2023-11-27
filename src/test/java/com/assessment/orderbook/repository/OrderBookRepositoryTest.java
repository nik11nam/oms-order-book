package com.assessment.orderbook.repository;

import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Instrument;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.entity.OrderBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.assessment.orderbook.MockTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderBookRepositoryTest {

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Test
    public void testFindByInstrumentId() {
        Optional<OrderBook> orderBookOptional = orderBookRepository.findByInstrumentId(2L);
        assertThat(orderBookOptional.isPresent()).isTrue();

        OrderBook orderBook = orderBookOptional.get();
        assertThat(orderBook.getInstrument().getId()).isEqualTo(2);
        assertThat(orderBook.getOrders().size()).isZero();
        assertThat(orderBook.getExecutions().size()).isZero();
    }

    @Test
    public void testUpdate() {
        Optional<OrderBook> orderBookOptional = orderBookRepository.findByInstrumentId(2L);
        assertThat(orderBookOptional.isPresent()).isTrue();

        OrderBook orderBook = orderBookOptional.get();
        assertThat(orderBook.getOrders().size()).isZero();

        Instrument instrument = mockInstrument(2L);

        Order order = mockMarketOrder(instrument);
        orderBook.addOrder(order);
        orderBookRepository.update(2L, orderBook);

        orderBook = orderBookRepository.findByInstrumentId(2L).get();
        assertThat(orderBook.getOrders().size()).isEqualTo(1);
        assertThat(orderBook.getOrders().get(0).getInstrument().getId()).isEqualTo(2);
    }
}
