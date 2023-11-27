package com.assessment.orderbook.validator;

import com.assessment.orderbook.repository.InstrumentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstrumentIdValidator implements ConstraintValidator<InstrumentId, Long> {
    @Autowired
    private InstrumentRepository instrumentRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return instrumentRepository.getById(id).isPresent();
    }
}
