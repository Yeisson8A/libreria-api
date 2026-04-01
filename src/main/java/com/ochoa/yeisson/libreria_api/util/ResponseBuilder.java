package com.ochoa.yeisson.libreria_api.util;

import com.ochoa.yeisson.libreria_api.response.ApiResponse;
import java.time.LocalDateTime;

public class ResponseBuilder {
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<?> error(String message, Object errors) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
