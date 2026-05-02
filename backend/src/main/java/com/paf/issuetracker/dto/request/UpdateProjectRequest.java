package com.paf.issuetracker.dto.request;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProjectRequest {
    @Size(min=2, max=100) private String name;
    private String description;
    private Boolean archived;
}
