package com.assessment.orderbook.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ApiErrorResponse {
    private HttpStatus status;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
    private String error;
    private String debugMessage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ApiValidationError> validationErrors = new ArrayList<>();

    public ApiErrorResponse(HttpStatus status) {
        timestamp = LocalDateTime.now();
        this.status = status;
    }

    public ApiErrorResponse(HttpStatus status, String error) {
        this(status);
        this.error = error;
    }

    public ApiErrorResponse(HttpStatus status, String error, Throwable t) {
        this(status, error);
        this.debugMessage = t.getMessage();
    }

    public void addValidationError(ApiValidationError validationError) {
        validationErrors.add(validationError);
    }

}
