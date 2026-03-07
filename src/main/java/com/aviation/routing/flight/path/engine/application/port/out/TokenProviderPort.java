package com.aviation.routing.flight.path.engine.application.port.out;

import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginResponse;

public interface TokenProviderPort {
    LoginResponse authenticate(String username, String password);
    LoginResponse refreshToken(String refreshToken);
}