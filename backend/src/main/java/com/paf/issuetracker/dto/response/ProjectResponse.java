package com.paf.issuetracker.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String projectKey;
    private UserResponse owner;
    private List<UserResponse> members;
    private boolean archived;
    private long totalIssues;
    private long openIssues;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
