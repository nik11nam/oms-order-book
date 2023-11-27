package com.assessment.orderbook;

import com.assessment.orderbook.constants.OrderType;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Instrument;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.entity.OrderBook;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MockTestUtils {

    public static Instrument mockInstrument(Long id) {
        return new Instrument(id, "Instrument-" + id);
    }

    public static Order mockMarketOrder(Instrument instrument) {
        return new Order(instrument, 100L, null, OrderType.MARKET, LocalDate.now());
    }

    public static Order mockLimitOrder(Instrument instrument) {
        return new Order(instrument, 100L, BigDecimal.valueOf(1.50), OrderType.LIMIT, LocalDate.now());
    }

    public static Execution mockExecution(Instrument instrument) {
        return new Execution(instrument, 100L, BigDecimal.valueOf(1.5));
    }

    public static OrderBook mockOrderBook(Instrument instrument) {
        return new OrderBook(instrument.getId(), instrument);
    }

}
