package com.assessment.orderbook.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QuantityValidator implements ConstraintValidator<Quantity, Long> {

    @Value("${order-book.quantity.min}")
    private Long minQuantity;

    @Value("${order-book.quantity.max}")
    private Long maxQuantity;

    @Override
    public boolean isValid(Long quantity, ConstraintValidatorContext context) {
        return quantity != null && quantity.compareTo(minQuantity) >= 0 && quantity.compareTo(maxQuantity) < 0;
    }
}
