package com.assessment.orderbook.controller;

import com.assessment.orderbook.dto.ApiMethodResponse;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.dto.ApiErrorResponse;
import com.assessment.orderbook.mapper.DtoEntityMapper;
import com.assessment.orderbook.constants.OrderBookStatus;
import com.assessment.orderbook.dto.ExecutionDto;
import com.assessment.orderbook.dto.OrderDto;
import com.assessment.orderbook.service.OrderBookService;
import com.assessment.orderbook.validator.InstrumentId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-book")
@Validated
@Slf4j
public class OrderBookController {
    @Autowired
    private OrderBookService orderBookService;

    @Autowired
    private DtoEntityMapper dtoEntityMapper;

    @Operation(summary = "Open / Close order book for given instrument id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order book status updated for instrument id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMethodResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid instrument id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Order book doesn't exist for instrument id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)) })
    })
    @PutMapping("/{id}/{status}")
    ResponseEntity<ApiMethodResponse> changeOrderBookStatus(@Parameter(description = "Instrument id of the order book") @PathVariable @InstrumentId Long id,
                                                    @Parameter(description = "Order book status") @PathVariable OrderBookStatus status) {
        orderBookService.modifyOrderBookStatus(id, status);

        String message = String.format("Order book is %s for instrument id: %d", (OrderBookStatus.OPEN == status ? "opened" : "closed"), id);
        log.info(message);
        return buildResponseEntity(message, HttpStatus.OK);
    }

    @Operation(summary = "Add order to the order book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order added to the order book",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMethodResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Request validation errors",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)) })
    })
    @PostMapping("/order")
    ResponseEntity<ApiMethodResponse> addOrderToBook(@Valid @RequestBody OrderDto orderDto) {
        Order order = dtoEntityMapper.orderDtoToOrder(orderDto);
        orderBookService.addOrderToBook(order);

        String message = String.format("Order successfully added to order book for instrument id: %d",
                orderDto.instrumentId());
        log.info(message);
        return buildResponseEntity(message, HttpStatus.CREATED);
    }

    @Operation(summary = "Add execution to the order book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Execution added to the order book",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMethodResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Request validation errors",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)) })
    })
    @PostMapping("/execution")
    ResponseEntity<ApiMethodResponse> addExecutionToBook(@Valid @RequestBody ExecutionDto executionDto) {
        Execution execution = dtoEntityMapper.executionDtoToExecution(executionDto);
        orderBookService.addExecutionToBook(execution);

        String message = String.format("Execution successfully added to order book for instrument id: %d",
                executionDto.instrumentId());
        log.info(message);
        return buildResponseEntity(message, HttpStatus.CREATED);
    }

    private ResponseEntity<ApiMethodResponse> buildResponseEntity(String message, HttpStatusCode status) {
        return new ResponseEntity<>(new ApiMethodResponse<>(message), status);
    }

}
