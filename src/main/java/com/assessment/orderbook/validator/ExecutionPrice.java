package com.assessment.orderbook.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExecutionPriceValidator.class)
public @interface ExecutionPrice {
    String message() default "{execution.price.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
