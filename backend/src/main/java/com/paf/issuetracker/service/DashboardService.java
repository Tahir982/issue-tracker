package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.response.DashboardStatsResponse;
import com.paf.issuetracker.enums.*;
import com.paf.issuetracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public DashboardStatsResponse getStats() {
        var cur    = userService.getCurrentUser();
        var all    = issueRepository.findAll();
        long total = all.size();
        Map<IssueStatus, Long> byStatus = Arrays.stream(IssueStatus.values())
               .collect(Collectors.toMap(s -> s, s -> all.stream().filter(i -> i.getStatus() == s).count()));
        Map<String, Long> byPriority = Arrays.stream(IssuePriority.values())
               .collect(Collectors.toMap(Enum::name, p -> all.stream().filter(i -> i.getPriority() == p).count()));
        Map<String, Long> byType = Arrays.stream(IssueType.values())
               .collect(Collectors.toMap(Enum::name, t -> all.stream().filter(i -> i.getType() == t).count()));

        return DashboardStatsResponse.builder()
               .totalProjects(projectRepository.findAllAccessibleByUser(cur).size())
               .totalIssues(total)
               .openIssues(byStatus.getOrDefault(IssueStatus.OPEN, 0L))
               .inProgressIssues(byStatus.getOrDefault(IssueStatus.IN_PROGRESS, 0L))
               .resolvedIssues(byStatus.getOrDefault(IssueStatus.RESOLVED, 0L))
               .closedIssues(byStatus.getOrDefault(IssueStatus.CLOSED, 0L))
               .myAssignedIssues(issueRepository.findByAssignee(cur).size())
               .myReportedIssues(issueRepository.findByReporter(cur).size())
               .issuesByPriority(byPriority).issuesByType(byType).build();
    }
}
