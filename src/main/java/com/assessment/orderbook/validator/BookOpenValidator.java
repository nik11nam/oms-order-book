package com.assessment.orderbook.validator;

import com.assessment.orderbook.constants.OrderBookStatus;
import com.assessment.orderbook.dto.OrderDto;
import com.assessment.orderbook.entity.OrderBook;
import com.assessment.orderbook.repository.OrderBookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookOpenValidator implements ConstraintValidator<BookOpen, OrderDto> {

    @Autowired
    OrderBookRepository orderBookRepository;

    @Override
    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext ctx) {
        Optional<OrderBook> result = orderBookRepository.findByInstrumentId(orderDto.instrumentId());

        if(result.isEmpty()) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("{orderbook.id.invalid}").addConstraintViolation();
        } else {
            OrderBook orderBook = result.get();
            return OrderBookStatus.OPEN == orderBook.getStatus();
        }
        return false;
    }


}
