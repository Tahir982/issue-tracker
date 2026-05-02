package com.paf.issuetracker.dto.request;
import com.paf.issuetracker.enums.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateIssueRequest {
    @Size(min=5, max=200) private String title;
    private String description;
    private IssueType type;
    private IssueStatus status;
    private IssuePriority priority;
    private Long assigneeId;
    private LocalDate dueDate;
    private Integer storyPoints;
}
