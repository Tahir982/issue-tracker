package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.response.UserResponse;
import com.paf.issuetracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User u) {
        if (u == null) return null;
        return UserResponse.builder()
               .id(u.getId()).username(u.getUsername()).email(u.getEmail())
               .fullName(u.getFullName()).role(u.getRole()).active(u.isActive())
               .createdAt(u.getCreatedAt()).build();
    }
}
