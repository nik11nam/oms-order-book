package com.assessment.orderbook.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstrumentIdValidator.class)
public @interface InstrumentId {
    String message() default "{instrument.id.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
