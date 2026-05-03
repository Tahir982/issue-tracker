package com.paf.issuetracker.dto.response;
import com.paf.issuetracker.enums.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class IssueResponse {
    private Long id;
    private String issueKey;
    private Integer issueNumber;
    private String title;
    private String description;
    private IssueType type;
    private IssueStatus status;
    private IssuePriority priority;
    private UserResponse reporter;
    private UserResponse assignee;
    private Long projectId;
    private String projectName;
    private String projectKey;
    private LocalDate dueDate;
    private Integer storyPoints;
    private long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
