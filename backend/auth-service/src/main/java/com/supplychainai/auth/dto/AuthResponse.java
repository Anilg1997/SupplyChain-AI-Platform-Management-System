package com.supplychainai.auth.dto;

import java.time.Instant;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    Instant expiresAt,
    String userId,
    String email,
    java.util.Set<String> roles
) {}
