package com.assessment.orderbook.service;

import com.assessment.orderbook.constants.OrderBookStatus;
import com.assessment.orderbook.constants.OrderType;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Instrument;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.entity.OrderBook;
import com.assessment.orderbook.exception.ItemNotFoundException;
import com.assessment.orderbook.repository.ExecutionRepository;
import com.assessment.orderbook.repository.OrderBookRepository;
import com.assessment.orderbook.repository.OrderRepository;
import com.assessment.orderbook.service.impl.OrderBookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.assessment.orderbook.MockTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderBookServiceTest {

    @Mock
    OrderBookRepository orderBookRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ExecutionRepository executionRepository;
    @InjectMocks
    OrderBookService orderBookService = new OrderBookServiceImpl();

    @Test
    public void testGetOrderBook() {
        Long instrumentId = 2L;
        Instrument instrument = mockInstrument(instrumentId);
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(mockOrderBook(instrument)));

        OrderBook orderBook = orderBookService.getOrderBook(instrumentId);

        // Mockito - verify method to capture method args for checks
        verify(orderBookRepository, times((1))).findByInstrumentId(instrumentId);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(orderBookRepository).findByInstrumentId(argumentCaptor.capture());
        Long capturedValue = argumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(instrumentId);

        assertThat(orderBook).isNotNull();
        assertThat(orderBook.getInstrument()).isEqualTo(instrument);
        assertThat(orderBook.getStatus()).isEqualTo(OrderBookStatus.OPEN);
    }

    @Test
    public void testGetOrderBookThrowsException() {
        Long instrumentId = 2L;
        when(orderBookRepository.findByInstrumentId(instrumentId))
                .thenThrow(new ItemNotFoundException("Order Book not available for instrument id: " + instrumentId));

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> orderBookService.getOrderBook(instrumentId),
                "Expected ItemNotFoundException to be thrown");
        assertThat(exception.getMessage()).contains("Order Book not available");
    }

    @Test
    public void testCloseOrderBookStatus() {
        Long instrumentId = 1L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);

        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));
        when(orderBookRepository.update(instrumentId, orderBook)).thenReturn(true);

        assertThat(orderBookService.modifyOrderBookStatus(instrumentId, OrderBookStatus.CLOSE))
                .isTrue();
    }

    @Test
    public void testOpenOrderBookStatus() {
        Long instrumentId = 1L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);

        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));
        when(orderBookRepository.update(instrumentId, orderBook)).thenReturn(true);

        assertThat(orderBookService.modifyOrderBookStatus(instrumentId, OrderBookStatus.OPEN)).isTrue();
    }

    @Test
    public void testModifyOrderBookStatusThrowsException() {
        Long instrumentId = 2L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);

        when(orderBookRepository.findByInstrumentId(instrumentId))
                .thenThrow(new ItemNotFoundException("Order Book not available for instrument id: " + instrumentId));
        when(orderBookRepository.update(instrumentId, orderBook)).thenReturn(true);

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> orderBookService.modifyOrderBookStatus(instrumentId, OrderBookStatus.CLOSE),
                "Expected ItemNotFoundException to be thrown");
        assertThat(exception.getMessage()).contains("Order Book not available");
    }

    @Test
    public void testAddOrderToBook() {
        Long instrumentId = 2L;
        Instrument instrument = mockInstrument(instrumentId);
        Long orderId = 5L;
        Order order = mockMarketOrder(instrument);

        when(orderRepository.save(order)).thenReturn(orderId);
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(mockOrderBook(instrument)));

        order = orderBookService.addOrderToBook(order);

        verify(orderRepository, times((1))).save(order);
        verify(orderBookRepository, times((1))).findByInstrumentId(instrumentId);

        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getInstrument()).isEqualTo(instrument);
        assertThat(order.getOrderType()).isEqualTo(OrderType.MARKET);
        assertThat(order.getPrice()).isNull();
    }

    @Test
    public void testAddExecutionToBook() {
        Long instrumentId = 2L;
        Instrument instrument = mockInstrument(instrumentId);
        Long executionId = 3L;
        Execution execution = mockExecution(instrument);

        when(executionRepository.save(execution)).thenReturn(executionId);
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(mockOrderBook(instrument)));

        execution = orderBookService.addExecutionToBook(execution);

        verify(executionRepository, times((1))).save(execution);
        verify(orderBookRepository, times((1))).findByInstrumentId(instrumentId);

        assertThat(execution.getId()).isEqualTo(executionId);
        assertThat(execution.getInstrument()).isEqualTo(instrument);
        assertThat(execution.getQuantity()).isNotNull();
        assertThat(execution.getPrice()).isNotNull();
    }
}
