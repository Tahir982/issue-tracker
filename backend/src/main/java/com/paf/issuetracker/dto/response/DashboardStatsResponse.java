package com.paf.issuetracker.dto.response;
import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data @Builder
public class DashboardStatsResponse {
    private long totalProjects;
    private long totalIssues;
    private long openIssues;
    private long inProgressIssues;
    private long resolvedIssues;
    private long closedIssues;
    private long myAssignedIssues;
    private long myReportedIssues;
    private Map<String, Long> issuesByPriority;
    private Map<String, Long> issuesByType;
}
