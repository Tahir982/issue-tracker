package com.paf.issuetracker.dto.request;
import com.paf.issuetracker.enums.IssueStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateIssueStatusRequest {
    @NotNull(message="Status is required")
    private IssueStatus status;
}
