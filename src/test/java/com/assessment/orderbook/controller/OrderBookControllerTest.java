package com.assessment.orderbook.controller;

import com.assessment.orderbook.constants.OrderBookStatus;
import com.assessment.orderbook.constants.OrderType;
import com.assessment.orderbook.dto.ExecutionDto;
import com.assessment.orderbook.dto.OrderDto;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Instrument;
import com.assessment.orderbook.entity.OrderBook;
import com.assessment.orderbook.mapper.DtoEntityMapper;
import com.assessment.orderbook.repository.InstrumentRepository;
import com.assessment.orderbook.repository.OrderBookRepository;
import com.assessment.orderbook.service.OrderBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.assessment.orderbook.MockTestUtils.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderBookController.class)
public class OrderBookControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderBookService orderBookService;
    @MockBean
    private DtoEntityMapper dtoEntityMapper;
    @MockBean
    private InstrumentRepository instrumentRepository;
    @MockBean
    private OrderBookRepository orderBookRepository;

    @Test
    public void testOpenOrderBook() throws Exception {
        Long instrumentId = 2L;
        Instrument instrument = mockInstrument(instrumentId);
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookService.modifyOrderBookStatus(instrumentId, OrderBookStatus.OPEN)).thenReturn(true);

        mockMvc.perform(put("/api/v1/order-book/" + instrumentId + "/open"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Order book is opened for instrument id: 2")));
    }

    @Test
    public void testCloseOrderBook() throws Exception {
        Long instrumentId = 5L;
        Instrument instrument = mockInstrument(instrumentId);
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookService.modifyOrderBookStatus(instrumentId, OrderBookStatus.CLOSE)).thenReturn(true);

        mockMvc.perform(put("/api/v1/order-book/" + instrumentId + "/close"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Order book is closed for instrument id: 5")));
    }

    @Test
    public void testInvalidOrderBookNotFound() throws Exception {
        Long instrumentId = 10L;
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.empty());
        when(orderBookService.modifyOrderBookStatus(instrumentId, OrderBookStatus.CLOSE)).thenReturn(true);

        mockMvc.perform(put("/api/v1/order-book/" + instrumentId + "/open"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.error", is("changeOrderBookStatus.id: Instrument not available for given id")));
    }


    @Test
    public void testAddMarketOrderToBook() throws Exception {
        Long instrumentId = 5L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));

        OrderDto orderDto = new OrderDto(instrumentId, 100L, null, OrderType.MARKET, LocalDate.now());
        mockMvc.perform(
                post("/api/v1/order-book/order")
                        .content(objectMapper.writeValueAsString(orderDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message",
                        is("Order successfully added to order book for instrument id: 5")));

    }

    @Test
    public void testAddLimitOrderToBook() throws Exception {
        Long instrumentId = 5L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));

        OrderDto orderDto = new OrderDto(instrumentId, 100L, BigDecimal.valueOf(23.5),
                OrderType.LIMIT, LocalDate.now());
        mockMvc.perform(
                        post("/api/v1/order-book/order")
                                .content(objectMapper.writeValueAsString(orderDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message",
                        is("Order successfully added to order book for instrument id: 5")));

    }

    @Test
    public void testAddOrderToClosedBookFailed() throws Exception {
        Long instrumentId = 5L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);
        orderBook.setStatus(OrderBookStatus.CLOSE);
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));

        OrderDto orderDto = new OrderDto(instrumentId, 100L, BigDecimal.valueOf(23.5),
                OrderType.LIMIT, LocalDate.now());
        mockMvc.perform(
                        post("/api/v1/order-book/order")
                                .content(objectMapper.writeValueAsString(orderDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.error", is("Validation errors")))
                .andExpect(jsonPath("$.validationErrors").isArray())
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        is("Order book is not opened for given id")));
    }

    @Test
    public void testAddExecutionToBook() throws Exception {
        Long instrumentId = 5L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);
        orderBook.setStatus(OrderBookStatus.CLOSE);
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));

        ExecutionDto executionDto = new ExecutionDto(instrumentId, 100L, BigDecimal.valueOf(23.5));
        mockMvc.perform(
                        post("/api/v1/order-book/execution")
                                .content(objectMapper.writeValueAsString(executionDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message",
                        is("Execution successfully added to order book for instrument id: 5")));
    }

    @Test
    public void testAddExecutionToOpenBookFailed() throws Exception {
        Long instrumentId = 5L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);
        orderBook.setStatus(OrderBookStatus.OPEN);
        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));

        ExecutionDto executionDto = new ExecutionDto(instrumentId, 100L, BigDecimal.valueOf(23.5));
        mockMvc.perform(
                        post("/api/v1/order-book/execution")
                                .content(objectMapper.writeValueAsString(executionDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.error", is("Validation errors")))
                .andExpect(jsonPath("$.validationErrors").isArray())
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        is("Order book is not closed for given id")));
    }

    @Test
    public void testAddDifferentExecutionPriceToBookFailed() throws Exception {
        Long instrumentId = 5L;
        Instrument instrument = mockInstrument(instrumentId);
        OrderBook orderBook = mockOrderBook(instrument);
        orderBook.setStatus(OrderBookStatus.CLOSE);
        orderBook.addExecution(new Execution(instrument, 100L, BigDecimal.valueOf(10)));
        orderBook.addExecution(new Execution(instrument, 15L, BigDecimal.valueOf(10)));

        when(instrumentRepository.getById(instrumentId)).thenReturn(Optional.of(instrument));
        when(orderBookRepository.findByInstrumentId(instrumentId)).thenReturn(Optional.of(orderBook));

        ExecutionDto executionDto = new ExecutionDto(instrumentId, 100L, BigDecimal.valueOf(23.5));
        mockMvc.perform(
                        post("/api/v1/order-book/execution")
                                .content(objectMapper.writeValueAsString(executionDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.error", is("Validation errors")))
                .andExpect(jsonPath("$.validationErrors").isArray())
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        is("Execution price should be same for all executions in an order book")));
    }

}
