package com.aviation.routing.flight.path.engine.common.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Generic API Response wrapper")
public class ApiResponse<T> {
    @Schema(description = "Payload of the response")
    private T data;
    @Schema(description = "Response code", example = "success")
    private String code;
    @Schema(description = "Informative message", example = "Operation completed successfully")
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