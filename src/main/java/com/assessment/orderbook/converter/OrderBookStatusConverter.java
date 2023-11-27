package com.assessment.orderbook.converter;

import com.assessment.orderbook.constants.OrderBookStatus;
import org.springframework.core.convert.converter.Converter;

public class OrderBookStatusConverter implements Converter<String, OrderBookStatus> {

    @Override
    public OrderBookStatus convert(String source) {
        return OrderBookStatus.valueOf(source.toUpperCase());
    }
}
