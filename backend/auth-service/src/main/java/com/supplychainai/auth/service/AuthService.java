package com.supplychainai.auth.service;

import com.supplychainai.auth.dto.AuthResponse;
import com.supplychainai.auth.dto.LoginRequest;
import com.supplychainai.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

    AuthResponse getCurrentUser(String userId);
}
