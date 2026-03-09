package com.aviation.routing.flight.path.engine.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aviation.routing.flight.path.engine.application.exception.ResourceNotFoundException;
import com.aviation.routing.flight.path.engine.common.payload.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBaseException_shouldReturnBadRequest() {
        BaseException ex = new ResourceNotFoundException(ErrorCode.SYS_VAL_001, "Test Message");
        ResponseEntity<ApiResponse<Void>> response = handler.handleBaseException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.SYS_VAL_001.name(), response.getBody().getCode());
        assertEquals("Test Message", response.getBody().getMessage());
    }

    @Test
    void handleBadCredentialsException_shouldReturnUnauthorized() {
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");
        ResponseEntity<ApiResponse<Void>> response = handler.handleBadCredentialsException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorCode.AUTH_ERR_001.name(), response.getBody().getCode());
    }

    @Test
    void handleAccessDeniedException_shouldReturnForbidden() {
        AccessDeniedException ex = new AccessDeniedException("Access Denied");
        ResponseEntity<ApiResponse<Void>> response = handler.handleAccessDeniedException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(ErrorCode.AUTH_ERR_002.name(), response.getBody().getCode());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        Exception ex = new Exception("Random error");
        ResponseEntity<ApiResponse<Void>> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCode.SYS_ERR_500.name(), response.getBody().getCode());
    }
}
