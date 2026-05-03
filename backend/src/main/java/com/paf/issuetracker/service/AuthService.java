package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.request.LoginRequest;
import com.paf.issuetracker.dto.request.RegisterRequest;
import com.paf.issuetracker.dto.response.AuthResponse;
import com.paf.issuetracker.entity.User;
import com.paf.issuetracker.enums.Role;
import com.paf.issuetracker.exception.DuplicateResourceException;
import com.paf.issuetracker.repository.UserRepository;
import com.paf.issuetracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername()))
            throw new DuplicateResourceException("Username already taken: " + req.getUsername());
        if (userRepository.existsByEmail(req.getEmail()))
            throw new DuplicateResourceException("Email already registered: " + req.getEmail());

        var user = User.builder()
               .username(req.getUsername()).email(req.getEmail())
               .password(passwordEncoder.encode(req.getPassword()))
               .fullName(req.getFullName()).role(Role.REPORTER).active(true).build();
        userRepository.save(user);
        log.info("Registered user: {}", user.getUsername());
        return buildAuth(user);
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        var user = userRepository.findByUsername(req.getUsername()).orElseThrow();
        log.info("Login: {}", req.getUsername());
        return buildAuth(user);
    }

    private AuthResponse buildAuth(User user) {
        var ud = userDetailsService.loadUserByUsername(user.getUsername());
        return AuthResponse.builder()
               .token(jwtTokenProvider.generateToken(ud))
               .tokenType("Bearer")
               .user(userMapper.toResponse(user)).build();
    }
}
