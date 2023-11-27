package com.assessment.orderbook.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public class ApiValidationError {
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiValidationError(String message) {
        this.message = message;
    }
}
