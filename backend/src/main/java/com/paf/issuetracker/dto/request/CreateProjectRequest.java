package com.paf.issuetracker.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank @Size(min=2, max=100)
    private String name;
    private String description;
    @NotBlank @Size(min=2, max=10)
    @Pattern(regexp="^[A-Z0-9]+$", message="Key must be uppercase letters/numbers only")
    private String projectKey;
}
