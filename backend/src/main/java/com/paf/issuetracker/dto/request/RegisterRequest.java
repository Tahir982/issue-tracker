package com.paf.issuetracker.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(min=3, max=50)
    @Pattern(regexp="^[a-zA-Z0-9_]+$", message="Only letters, numbers and underscores allowed")
    private String username;

    @NotBlank @Email(message="Invalid email")
    private String email;

    @NotBlank @Size(min=6, message="Password must be at least 6 characters")
    private String password;

    @NotBlank @Size(min=2, max=100)
    private String fullName;
}
