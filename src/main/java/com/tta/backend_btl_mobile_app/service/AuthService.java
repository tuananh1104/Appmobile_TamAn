package com.tta.backend_btl_mobile_app.service;

import com.tta.backend_btl_mobile_app.dto.request.LoginRequest;
import com.tta.backend_btl_mobile_app.dto.request.RegisterRequest;
import com.tta.backend_btl_mobile_app.dto.response.AuthResponse;
import com.tta.backend_btl_mobile_app.entity.User;
import com.tta.backend_btl_mobile_app.exception.UnauthorizedException;
import com.tta.backend_btl_mobile_app.repository.UserRepository;
import com.tta.backend_btl_mobile_app.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getRole().name(),
            jwtTokenProvider.getExpirationTime()
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName() != null ? request.getDisplayName() : request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);
        user.setIsActive(true);
        user.setThemeMode(User.ThemeMode.LIGHT);

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getRole().name(),
            jwtTokenProvider.getExpirationTime()
        );
    }
}
