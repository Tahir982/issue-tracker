package com.paf.issuetracker.dto.request;
import lombok.Data;
@Data
public class AssignIssueRequest {
    private Long assigneeId; // null = unassign
}
