package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import com.aviation.routing.flight.path.engine.common.exception.BaseException;
import com.aviation.routing.flight.path.engine.common.exception.ErrorCode;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LoginResponse;
import com.aviation.routing.flight.path.engine.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtTokenAdapterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtTokenAdapter jwtTokenAdapter;

    @Test
    void authenticate_withValidCredentials_shouldReturnTokens() {
        UserDetails userDetails = new User("admin", "password", Collections.emptyList());

        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh-token");

        LoginResponse response = jwtTokenAdapter.authenticate("admin", "admin123");

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_withInvalidCredentials_shouldThrowException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> jwtTokenAdapter.authenticate("admin", "wrong"))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void refreshToken_withValidToken_shouldReturnNewAccessToken() {
        UserDetails userDetails = new User("admin", "password", Collections.emptyList());

        when(jwtService.extractUsername("valid-refresh-token")).thenReturn("admin");
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid-refresh-token", userDetails)).thenReturn(true);
        when(jwtService.generateToken(userDetails)).thenReturn("new-access-token");

        LoginResponse response = jwtTokenAdapter.refreshToken("valid-refresh-token");

        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isEqualTo("valid-refresh-token");
    }

    @Test
    void refreshToken_withInvalidToken_shouldThrowException() {
        UserDetails userDetails = new User("admin", "password", Collections.emptyList());

        when(jwtService.extractUsername("invalid-token")).thenReturn("admin");
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(jwtService.isTokenValid("invalid-token", userDetails)).thenReturn(false);

        assertThatThrownBy(() -> jwtTokenAdapter.refreshToken("invalid-token"))
                .isInstanceOf(BaseException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.AUTH_ERR_003);
    }
}
