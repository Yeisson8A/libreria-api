package com.ochoa.yeisson.libreria_api.exception;

import com.ochoa.yeisson.libreria_api.response.ApiResponse;
import com.ochoa.yeisson.libreria_api.util.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // VALIDACIONES
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseBuilder.error("Error de validación", errors);
    }

    // NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseBuilder.error(ex.getMessage(), null);
    }

    // BAD REQUEST
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return ResponseBuilder.error(ex.getMessage(), null);
    }

    // BUSINESS
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponse<?> handleBusiness(BusinessException ex, HttpServletRequest request) {
        return ResponseBuilder.error(ex.getMessage(), null);
    }

    // ERROR GENERAL
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGeneral(Exception ex, HttpServletRequest request) {
        return ResponseBuilder.error("Error interno del servidor", null);
    }
}
