package com.aviation.routing.flight.path.engine.application.port.in;

import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginRequest;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RefreshTokenRequest;

public interface AuthUseCase {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
}
