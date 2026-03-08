package com.aviation.routing.flight.path.engine.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 604800000L);
    }

    @Test
    void generateToken_shouldIncludeJtiAndRoles() {
        // Given
        UserDetails userDetails = new User("testuser", "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);

        Claims claims = jwtService.extractClaim(token, claims1 -> claims1);
        assertNotNull(claims.getId(), "JTI should not be null");
        
        List<String> roles = claims.get("roles", List.class);
        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    void generateRefreshToken_shouldIncludeJti() {
        // Given
        UserDetails userDetails = new User("testuser", "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        // When
        String token = jwtService.generateRefreshToken(userDetails);

        // Then
        assertNotNull(token);
        Claims claims = jwtService.extractClaim(token, claims1 -> claims1);
        assertNotNull(claims.getId(), "JTI should not be null in refresh token");
    }
}
