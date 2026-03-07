package com.aviation.routing.flight.path.engine.application.service.impl;

import com.aviation.routing.flight.path.engine.application.port.in.AuthUseCase;
import com.aviation.routing.flight.path.engine.application.port.out.TokenProviderPort;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginRequest;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements AuthUseCase {
    private final TokenProviderPort tokenProviderPort;

    @Override
    public LoginResponse login(LoginRequest request) {
        return tokenProviderPort.authenticate(request.username(), request.password());
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        return tokenProviderPort.refreshToken(request.refreshToken());
    }
}
