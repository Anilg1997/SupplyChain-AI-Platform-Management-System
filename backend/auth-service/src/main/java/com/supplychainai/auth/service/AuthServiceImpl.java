package com.supplychainai.auth.service;

import com.supplychainai.auth.config.JwtTokenProvider;
import com.supplychainai.auth.dto.AuthResponse;
import com.supplychainai.auth.dto.LoginRequest;
import com.supplychainai.auth.dto.RegisterRequest;
import com.supplychainai.auth.model.RefreshToken;
import com.supplychainai.auth.model.Role;
import com.supplychainai.auth.model.User;
import com.supplychainai.auth.repository.RefreshTokenRepository;
import com.supplychainai.auth.repository.RoleRepository;
import com.supplychainai.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        user.setDepartment(request.getDepartment());

        Set<Role> roles = new HashSet<>();
        String roleName = request.getRole() != null ? request.getRole() : "ROLE_WAREHOUSE_STAFF";
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
        roles.add(role);
        user.setRoles(roles);

        user = userRepository.save(user);
        log.info("User registered: {} with role: {}", user.getUsername(), roleName);

        return buildAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Account is disabled");
        }

        log.info("User logged in: {}", user.getUsername());
        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByUserId(
                UUID.fromString(jwtTokenProvider.getUserIdFromToken(refreshToken))
        );
        log.info("User logged out");
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse getCurrentUser(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new AuthResponse(
                null, null, null,
                user.getId().toString(),
                user.getEmail(),
                new HashSet<>(roles)
        );
    }

    private AuthResponse buildAuthResponse(User user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(), user.getEmail(), roles, user.getDepartment());

        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        Instant expiresAt = Instant.now().plusMillis(jwtTokenProvider.getAccessTokenExpiration());

        // Store refresh token hash
        RefreshToken rt = new RefreshToken(
                user.getId(),
                passwordEncoder.encode(refreshToken),
                LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault())
        );
        refreshTokenRepository.save(rt);

        return new AuthResponse(
                accessToken, refreshToken, expiresAt,
                user.getId().toString(),
                user.getEmail(),
                new HashSet<>(roles)
        );
    }
}
