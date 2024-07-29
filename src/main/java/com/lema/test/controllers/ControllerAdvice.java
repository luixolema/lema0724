package com.lema.test.controllers;

import com.lema.test.exceptions.ApiError;
import com.lema.test.exceptions.ApiSubError;
import com.lema.test.exceptions.ApiValidationError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    private static ResponseEntity<Object> getObjectResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
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

        return getObjectResponseEntity(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return getObjectResponseEntity(apiError);
    }
}
