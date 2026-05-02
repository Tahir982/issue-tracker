package com.paf.issuetracker.controller;

import com.paf.issuetracker.dto.response.*;
import com.paf.issuetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/users") @RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping        public ResponseEntity<ApiResponse<List<UserResponse>>> all() { return ResponseEntity.ok(ApiResponse.success("OK", userService.getAllUsers())); }
    @GetMapping("/me") public ResponseEntity<ApiResponse<UserResponse>> me()        { return ResponseEntity.ok(ApiResponse.success("OK", userService.getCurrentUserProfile())); }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<UserResponse>> byId(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success("OK", userService.getUserById(id))); }
}
