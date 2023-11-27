package com.assessment.orderbook.validator;

import com.assessment.orderbook.constants.OrderType;
import com.assessment.orderbook.dto.OrderDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LimitOrderPriceValidator implements ConstraintValidator<LimitOrderPrice, OrderDto> {

    @Value("${order-book.price.min}")
    private BigDecimal minPrice;

    @Value("${order-book.price.max}")
    private BigDecimal maxPrice;

    @Override
    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        if (OrderType.MARKET == orderDto.orderType())
            return true;

        BigDecimal price = orderDto.price();
        return price != null && (price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) < 0);
    }
}
