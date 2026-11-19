package com.tharusha.ironbulk.service;

import com.tharusha.ironbulk.dto.AuthResponse;
import com.tharusha.ironbulk.dto.LoginRequest;
import com.tharusha.ironbulk.dto.RegisterRequest;
import com.tharusha.ironbulk.entity.User;
import com.tharusha.ironbulk.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        if (request.dailyCalorieTarget() != null) {
            user.setDailyCalorieTarget(request.dailyCalorieTarget());
        }
        if (request.dailyProteinTarget() != null) {
            user.setDailyProteinTarget(request.dailyProteinTarget());
        }

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getUsername());
        return new AuthResponse(token, saved.getUsername(), saved.getDailyCalorieTarget(), saved.getDailyProteinTarget());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), user.getDailyCalorieTarget(), user.getDailyProteinTarget());
    }
}