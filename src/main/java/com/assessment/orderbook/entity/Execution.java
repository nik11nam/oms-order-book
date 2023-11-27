package com.assessment.orderbook.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class Execution {
    @Setter
    private Long id; // will be set when persisting
    private final Instrument instrument;
    private final Long quantity;
    private final BigDecimal price;

    public Execution(Instrument instrument, Long quantity, BigDecimal price) {
        this.instrument = instrument;
        this.quantity = quantity;
        this.price = price;
    }
}
