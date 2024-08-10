package com.lema.test.components.web.internal.rest;

import com.lema.test.components.web.internal.exceptions.ApiError;
import com.lema.test.components.web.internal.exceptions.ApiSubError;
import com.lema.test.components.web.internal.exceptions.ApiValidationError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ApiSubError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> (ApiSubError) new ApiValidationError(
                        e.getObjectName(),
                        e.getField(),
                        e.getRejectedValue(),
                        e.getDefaultMessage()
                ))
                .toList();

        ApiError apiError = new ApiError("Invalid Data");
        apiError.setSubErrors(errors);

        return apiError;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiError handleEntityNotFound(EntityNotFoundException ex) {
        return new ApiError(ex.getLocalizedMessage());
    }
}
