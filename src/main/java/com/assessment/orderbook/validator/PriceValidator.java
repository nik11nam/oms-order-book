package com.assessment.orderbook.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceValidator implements ConstraintValidator<Price, BigDecimal> {

    @Value("${order-book.price.min}")
    private BigDecimal minPrice;

    @Value("${order-book.price.max}")
    private BigDecimal maxPrice;

    @Override
    public boolean isValid(BigDecimal price, ConstraintValidatorContext constraintValidatorContext) {
        return price != null && price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) < 0;
    }
}
