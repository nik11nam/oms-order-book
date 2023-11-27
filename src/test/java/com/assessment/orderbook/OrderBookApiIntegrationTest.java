package com.assessment.orderbook;

import com.assessment.orderbook.constants.OrderBookStatus;
import com.assessment.orderbook.constants.OrderType;
import com.assessment.orderbook.dto.*;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.entity.OrderBook;
import com.assessment.orderbook.repository.OrderBookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderBookApiIntegrationTest {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static HttpHeaders headers;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private OrderBookRepository orderBookRepository;


    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testOpenOrderBook() {
        Long instrumentId = 2L;
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ApiMethodResponse> response = restTemplate.exchange(
                (createURLWithPort() + "/" + instrumentId + "/open"), HttpMethod.PUT, entity, ApiMethodResponse.class);

        ApiMethodResponse apiMethodResponse = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiMethodResponse).isNotNull();
        assertThat(apiMethodResponse.getMessage()).isEqualTo("Order book is opened for instrument id: 2");

        Optional<OrderBook> orderBookOptional = orderBookRepository.findByInstrumentId(instrumentId);
        assertThat(orderBookOptional.isPresent()).isTrue();
        OrderBook orderBook = orderBookOptional.get();
        assertThat(orderBook.getStatus()).isEqualTo(OrderBookStatus.OPEN);
    }

    @Test
    public void testCloseOrderBook() {
        Long instrumentId = 2L;
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ApiMethodResponse> response = restTemplate.exchange(
                (createURLWithPort() + "/" + instrumentId + "/close"), HttpMethod.PUT, entity, ApiMethodResponse.class);

        ApiMethodResponse apiMethodResponse = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiMethodResponse).isNotNull();
        assertThat(apiMethodResponse.getMessage()).isEqualTo("Order book is closed for instrument id: 2");

        Optional<OrderBook> orderBookOptional = orderBookRepository.findByInstrumentId(instrumentId);
        assertThat(orderBookOptional.isPresent()).isTrue();
        OrderBook orderBook = orderBookOptional.get();
        assertThat(orderBook.getStatus()).isEqualTo(OrderBookStatus.CLOSE);
    }

    @Test
    public void testAddOrderToBook() throws Exception {
        Long instrumentId = 5L;
        OrderDto orderDto = new OrderDto(instrumentId, 100L, null, OrderType.MARKET, LocalDate.now());

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(orderDto), headers);
        ResponseEntity<ApiMethodResponse> response = restTemplate.exchange(
                (createURLWithPort() + "/order"), HttpMethod.POST, entity, ApiMethodResponse.class);

        ApiMethodResponse apiMethodResponse = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiMethodResponse).isNotNull();
        assertThat(apiMethodResponse.getMessage())
                .isEqualTo("Order successfully added to order book for instrument id: 5");

        Optional<OrderBook> orderBookOptional = orderBookRepository.findByInstrumentId(instrumentId);
        assertThat(orderBookOptional.isPresent()).isTrue();
        OrderBook orderBook = orderBookOptional.get();
        assertThat(orderBook.getStatus()).isEqualTo(OrderBookStatus.OPEN);
        assertThat(orderBook.getOrders().size()).isNotZero();

        Order order = orderBook.getOrders().get(0);
        assertThat(order.getId()).isEqualTo(1);
        assertThat(order.getOrderType()).isEqualTo(OrderType.MARKET);
        assertThat(order.getPrice()).isNull();
    }

    @Test
    public void testAddOrderToBookWithFieldValidationErrors() throws Exception {
        Long instrumentId = 5L;
        OrderDto orderDto = new OrderDto(instrumentId, 100L, null, OrderType.LIMIT, LocalDate.now());

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(orderDto), headers);
        ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(
                (createURLWithPort() + "/order"), HttpMethod.POST, entity, ApiErrorResponse.class);

        ApiErrorResponse apiErrorResponse = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(apiErrorResponse).isNotNull();
        assertThat(apiErrorResponse.getError()).isEqualTo("Validation errors");

        List<ApiValidationError> validationErrors = apiErrorResponse.getValidationErrors();
        assertThat(validationErrors).isNotNull();
        assertThat(validationErrors.size()).isEqualTo(1);
        ApiValidationError validationError = validationErrors.get(0);
        assertThat(validationError.getMessage())
                .isEqualTo("For Limit order, price range >= 0.1 and < 100000 is mandatory");
    }

    @Test
    public void testAddExecutionToOrderBook() throws Exception {
        Long instrumentId = 5L;
        ExecutionDto executionDto = new ExecutionDto(instrumentId, 100L, BigDecimal.valueOf(10));

        // Change status of order book to CLOSE to accept executions
        Optional<OrderBook> orderBookOptional = orderBookRepository.findByInstrumentId(instrumentId);
        assertThat(orderBookOptional.isPresent()).isTrue();
        OrderBook orderBook = orderBookOptional.get();
        orderBook.setStatus(OrderBookStatus.CLOSE);
        orderBookRepository.update(instrumentId, orderBook);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(executionDto), headers);
        ResponseEntity<ApiMethodResponse> response = restTemplate.exchange(
                (createURLWithPort() + "/execution"), HttpMethod.POST, entity, ApiMethodResponse.class);

        ApiMethodResponse apiMethodResponse = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiMethodResponse).isNotNull();
        assertThat(apiMethodResponse.getMessage())
                .isEqualTo("Execution successfully added to order book for instrument id: 5");

        orderBookOptional = orderBookRepository.findByInstrumentId(instrumentId);
        assertThat(orderBookOptional.isPresent()).isTrue();
        orderBook = orderBookOptional.get();
        assertThat(orderBook.getStatus()).isEqualTo(OrderBookStatus.CLOSE);
        assertThat(orderBook.getExecutions().size()).isNotZero();

        Execution execution = orderBook.getExecutions().get(0);
        assertThat(execution.getId()).isEqualTo(1);
        assertThat(execution.getQuantity()).isEqualTo(100);
        assertThat(execution.getPrice()).isEqualTo(BigDecimal.valueOf(10));
    }

    @Test
    public void testAddExecutionToBookFailedForDiffPrices() throws Exception {
        Long instrumentId = 5L;
        ExecutionDto e1 = new ExecutionDto(instrumentId, 100L, BigDecimal.valueOf(10));

        // Change status of order book to CLOSE to accept executions
        Optional<OrderBook> orderBookOptional = orderBookRepository.findByInstrumentId(instrumentId);
        assertThat(orderBookOptional.isPresent()).isTrue();
        OrderBook orderBook = orderBookOptional.get();
        orderBook.setStatus(OrderBookStatus.CLOSE);
        orderBookRepository.update(instrumentId, orderBook);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(e1), headers);
        ResponseEntity<ApiMethodResponse> response = restTemplate.exchange(
                (createURLWithPort() + "/execution"), HttpMethod.POST, entity, ApiMethodResponse.class);

        ApiMethodResponse apiMethodResponse = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiMethodResponse).isNotNull();
        assertThat(apiMethodResponse.getMessage())
                .isEqualTo("Execution successfully added to order book for instrument id: 5");

        ExecutionDto e2 = new ExecutionDto(instrumentId, 100L, BigDecimal.valueOf(20));
        entity = new HttpEntity<>(objectMapper.writeValueAsString(e2), headers);
        ResponseEntity<ApiErrorResponse> errorResponse = restTemplate.exchange(
                (createURLWithPort() + "/execution"), HttpMethod.POST, entity, ApiErrorResponse.class);

        ApiErrorResponse apiErrorResponse = errorResponse.getBody();
        assertThat(apiErrorResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(apiErrorResponse).isNotNull();
        assertThat(apiErrorResponse.getError()).isEqualTo("Validation errors");

        List<ApiValidationError> validationErrors = apiErrorResponse.getValidationErrors();
        assertThat(validationErrors).isNotNull();
        assertThat(validationErrors.size()).isEqualTo(1);
        ApiValidationError validationError = validationErrors.get(0);
        assertThat(validationError.getMessage())
                .isEqualTo("Execution price should be same for all executions in an order book");
    }


    private String createURLWithPort() {
        return "http://localhost:" + port + "/oms/api/v1/order-book";
    }

}
