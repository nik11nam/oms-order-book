package com.assessment.orderbook.dto;

import com.assessment.orderbook.constants.OrderType;
import com.assessment.orderbook.validator.BookOpen;
import com.assessment.orderbook.validator.InstrumentId;
import com.assessment.orderbook.validator.LimitOrderPrice;
import com.assessment.orderbook.validator.Quantity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

@LimitOrderPrice
@BookOpen
public record OrderDto(@NotNull(message = "{instrument.id.required}")
                       @InstrumentId Long instrumentId,
                       @NotNull(message = "{quantity.required}")
                       @Quantity Long quantity,
                       BigDecimal price,
                       @NotNull(message = "{order.type.required}") OrderType orderType,
                       @NotNull(message = "{entry.date.required}")
                       @PastOrPresent(message = "{entry.date.invalid}")
                       @JsonSerialize(using = LocalDateSerializer.class)
                       @JsonDeserialize(using = LocalDateDeserializer.class)
                       LocalDate entryDate) {

}
