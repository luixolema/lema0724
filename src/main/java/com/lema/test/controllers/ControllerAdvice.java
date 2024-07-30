package com.lema.test.controllers;

import com.lema.test.exceptions.ApiError;
import com.lema.test.exceptions.ApiSubError;
import com.lema.test.exceptions.ApiValidationError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
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

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Data");
        apiError.setSubErrors(errors);

        return apiError;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ApiError handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return apiError;
    }
}
