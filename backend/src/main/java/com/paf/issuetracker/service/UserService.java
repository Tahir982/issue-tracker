package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.response.UserResponse;
import com.paf.issuetracker.entity.User;
import com.paf.issuetracker.exception.ResourceNotFoundException;
import com.paf.issuetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getCurrentUser() {
        var details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(details.getUsername())
               .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(userRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    public UserResponse getCurrentUserProfile() { return userMapper.toResponse(getCurrentUser()); }
}
