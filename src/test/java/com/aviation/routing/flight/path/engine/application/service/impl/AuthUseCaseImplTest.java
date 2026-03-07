package com.aviation.routing.flight.path.engine.application.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.aviation.routing.flight.path.engine.application.port.out.TokenProviderPort;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginRequest;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RefreshTokenRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseImplTest {

    @Mock
    private TokenProviderPort tokenProviderPort;

    @InjectMocks
    private AuthUseCaseImpl authUseCase;

    @Test
    void login_shouldCallTokenProvider() {
        LoginRequest request = new LoginRequest("admin", "admin123");
        LoginResponse expectedResponse = LoginResponse.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        when(tokenProviderPort.authenticate("admin", "admin123")).thenReturn(expectedResponse);

        LoginResponse actualResponse = authUseCase.login(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void refreshToken_shouldCallTokenProvider() {
        RefreshTokenRequest request = new RefreshTokenRequest("refresh-token");
        LoginResponse expectedResponse = LoginResponse.builder()
                .accessToken("new-access")
                .refreshToken("refresh-token")
                .build();

        when(tokenProviderPort.refreshToken("refresh-token")).thenReturn(expectedResponse);

        LoginResponse actualResponse = authUseCase.refreshToken(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
