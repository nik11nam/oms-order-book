package com.assessment.orderbook.validator;

import com.assessment.orderbook.dto.ExecutionDto;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.OrderBook;
import com.assessment.orderbook.repository.OrderBookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ExecutionPriceValidator implements ConstraintValidator<ExecutionPrice, ExecutionDto> {

    @Autowired
    OrderBookRepository orderBookRepository;

    @Override
    public boolean isValid(ExecutionDto executionDto, ConstraintValidatorContext ctx) {

        Optional<OrderBook> result = orderBookRepository.findByInstrumentId(executionDto.instrumentId());

        if(result.isEmpty()) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Order book not available for given instrument id").addConstraintViolation();
            return false;
        } else {
            OrderBook orderBook = result.get();
            List<Execution> executions = orderBook.getExecutions();
            BigDecimal price = executionDto.price();

            return executions.stream()
                    .allMatch(execution -> execution.getPrice().compareTo(price) == 0);
        }
    }
}
