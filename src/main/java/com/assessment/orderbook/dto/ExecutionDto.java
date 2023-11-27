package com.assessment.orderbook.dto;

import com.assessment.orderbook.validator.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@BookClosed
@ExecutionPrice
public record ExecutionDto(@NotNull(message = "{instrument.id.required}")
                           @InstrumentId Long instrumentId,
                           @NotNull(message = "{quantity.required}")
                           @Quantity Long quantity,
                           @NotNull(message = "{price.required}")
                           @Price BigDecimal price) {
}
