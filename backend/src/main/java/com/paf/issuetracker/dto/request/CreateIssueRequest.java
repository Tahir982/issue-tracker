package com.paf.issuetracker.dto.request;
import com.paf.issuetracker.enums.IssuePriority;
import com.paf.issuetracker.enums.IssueType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateIssueRequest {
    @NotBlank @Size(min=5, max=200) private String title;
    private String description;
    @NotNull private IssueType type;
    @NotNull private IssuePriority priority;
    private Long assigneeId;
    private LocalDate dueDate;
    @Min(1) @Max(100) private Integer storyPoints;
}
