package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String code;
    private String message;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
            .code("success")
            .message(message)
            .data(data)
            .build();
    }

    public static ApiResponse<Void> error(String code, String message) {
        return ApiResponse.<Void>builder()
            .code(code)
            .message(message)
            .build();
    }
}