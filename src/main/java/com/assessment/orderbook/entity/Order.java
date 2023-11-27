package com.assessment.orderbook.entity;

import com.assessment.orderbook.constants.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Order {
    @Setter
    private Long id; // will be set when persisting
    private final Instrument instrument;
    private final Long quantity;
    private final BigDecimal price;
    private final OrderType orderType;
    private final LocalDate entryDate;

    public Order(Instrument instrument, Long quantity, BigDecimal price, OrderType orderType, LocalDate entryDate) {
        this.instrument = instrument;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
        this.entryDate = entryDate;
    }
}
