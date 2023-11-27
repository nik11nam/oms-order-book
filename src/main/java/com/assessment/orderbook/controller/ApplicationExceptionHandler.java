package com.assessment.orderbook.controller;

import com.assessment.orderbook.dto.ApiErrorResponse;
import com.assessment.orderbook.dto.ApiValidationError;
import com.assessment.orderbook.exception.ItemNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String error = "Malformed JSON request";
        log.warn(error, ex);

        return buildResponseEntity(new ApiErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String error = "Validation errors";
        log.warn(error, ex);

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, error, ex);
        ex.getBindingResult().getAllErrors().forEach((e) -> {
            ApiValidationError validationError = null;
            String message = e.getDefaultMessage();
            if (e instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                Object rejectedValue = fieldError.getRejectedValue();
                validationError = new ApiValidationError(fieldName, rejectedValue, message);
            } else {
                validationError = new ApiValidationError(message);
            }

            apiErrorResponse.addValidationError(validationError);
        });
        return buildResponseEntity(apiErrorResponse);
    }

    @ExceptionHandler({ItemNotFoundException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<Object> handleNotFoundException(Exception ex) {
        log.warn(ex.getMessage());
        return buildResponseEntity(new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<Object> handleUnexpectedException(RuntimeException ex) {
        String message = "An unexpected error occurred, please contact support team";
        log.error(message, ex);

        return buildResponseEntity(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiErrorResponse) {
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }
}
