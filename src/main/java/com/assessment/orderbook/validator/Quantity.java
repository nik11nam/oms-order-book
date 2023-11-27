package com.assessment.orderbook.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = QuantityValidator.class)
public @interface Quantity {
    String message() default "{quantity.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
