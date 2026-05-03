package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.request.RegisterRequest;
import com.paf.issuetracker.dto.response.AuthResponse;
import com.paf.issuetracker.entity.User;
import com.paf.issuetracker.enums.Role;
import com.paf.issuetracker.exception.DuplicateResourceException;
import com.paf.issuetracker.repository.UserRepository;
import com.paf.issuetracker.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock AuthenticationManager authManager;
    @Mock UserDetailsService userDetailsService;
    @Mock UserMapper userMapper;
    @InjectMocks AuthService authService;

    @Test
    void register_success() {
        var req = new RegisterRequest();
        req.setUsername("alice"); req.setEmail("alice@test.com");
        req.setPassword("pass123"); req.setFullName("Alice");

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0); u.setId(1L); u.setRole(Role.REPORTER); return u;
        });
        UserDetails mockUd = new org.springframework.security.core.userdetails.User(
                "alice",
                "hashed",
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_REPORTER"))
        );
        when(userDetailsService.loadUserByUsername("alice")).thenReturn(mockUd);
        when(jwtTokenProvider.generateToken(any())).thenReturn("jwt.token.here");

        AuthResponse result = authService.register(req);
        assertThat(result.getToken()).isEqualTo("jwt.token.here");
        assertThat(result.getTokenType()).isEqualTo("Bearer");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateUsername_throws() {
        var req = new RegisterRequest();
        req.setUsername("taken"); req.setEmail("x@y.com");
        when(userRepository.existsByUsername("taken")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> authService.register(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_duplicateEmail_throws() {
        var req = new RegisterRequest();
        req.setUsername("fresh"); req.setEmail("dup@y.com");
        when(userRepository.existsByUsername("fresh")).thenReturn(false);
        when(userRepository.existsByEmail("dup@y.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> authService.register(req));
        verify(userRepository, never()).save(any());
    }
}
