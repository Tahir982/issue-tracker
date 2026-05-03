package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.request.*;
import com.paf.issuetracker.dto.response.IssueResponse;
import com.paf.issuetracker.entity.*;
import com.paf.issuetracker.enums.IssueStatus;
import com.paf.issuetracker.exception.*;
import com.paf.issuetracker.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<IssueResponse> getByProject(Long projectId) {
        var p = findProject(projectId);
        return issueRepository.findByProjectOrderByCreatedAtDesc(p).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IssueResponse getById(Long id) { return toResponse(findIssue(id)); }

    public IssueResponse create(Long projectId, CreateIssueRequest req) {
        var project  = findProject(projectId);
        var reporter = userService.getCurrentUser();
        Integer max  = issueRepository.findMaxIssueNumberByProject(project);
        int num = (max == null) ? 1 : max + 1;

        User assignee = null;
        if (req.getAssigneeId() != null)
            assignee = userRepository.findById(req.getAssigneeId())
                       .orElseThrow(() -> new ResourceNotFoundException("User", req.getAssigneeId()));

        var issue = Issue.builder()
               .title(req.getTitle()).description(req.getDescription())
               .type(req.getType()).status(IssueStatus.OPEN).priority(req.getPriority())
               .project(project).reporter(reporter).assignee(assignee)
               .issueNumber(num).dueDate(req.getDueDate()).storyPoints(req.getStoryPoints()).build();
        issueRepository.save(issue);
        log.info("Issue {}-{} created", project.getProjectKey(), num);
        return toResponse(issue);
    }

    public IssueResponse update(Long id, UpdateIssueRequest req) {
        var issue = findIssue(id); assertCanModify(issue);
        if (req.getTitle()       != null) issue.setTitle(req.getTitle());
        if (req.getDescription() != null) issue.setDescription(req.getDescription());
        if (req.getType()        != null) issue.setType(req.getType());
        if (req.getStatus()      != null) issue.setStatus(req.getStatus());
        if (req.getPriority()    != null) issue.setPriority(req.getPriority());
        if (req.getDueDate()     != null) issue.setDueDate(req.getDueDate());
        if (req.getStoryPoints() != null) issue.setStoryPoints(req.getStoryPoints());
        if (req.getAssigneeId()  != null) {
            var u = userRepository.findById(req.getAssigneeId())
                   .orElseThrow(() -> new ResourceNotFoundException("User", req.getAssigneeId()));
            issue.setAssignee(u);
        }
        return toResponse(issue);
    }

    public IssueResponse updateStatus(Long id, UpdateIssueStatusRequest req) {
        var issue = findIssue(id);
        issue.setStatus(req.getStatus());
        return toResponse(issue);
    }

    public IssueResponse assign(Long id, AssignIssueRequest req) {
        var issue = findIssue(id);
        if (req.getAssigneeId() == null) { issue.setAssignee(null); }
        else {
            var u = userRepository.findById(req.getAssigneeId())
                   .orElseThrow(() -> new ResourceNotFoundException("User", req.getAssigneeId()));
            issue.setAssignee(u);
        }
        return toResponse(issue);
    }

    public void delete(Long id) { var i = findIssue(id); assertCanModify(i); issueRepository.delete(i); }

    @Transactional(readOnly = true)
    public List<IssueResponse> myAssigned() {
        return issueRepository.findByAssignee(userService.getCurrentUser()).stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── helpers ──────────────────────────────────────
    private Project findProject(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }
    private Issue findIssue(Long id) {
        return issueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Issue", id));
    }
    private void assertCanModify(Issue i) {
        var cur = userService.getCurrentUser();
        boolean ok = i.getReporter().getId().equals(cur.getId())
                  || i.getProject().getOwner().getId().equals(cur.getId())
                  || cur.getRole().name().equals("ADMIN")
                  || cur.getRole().name().equals("PROJECT_MANAGER");
        if (!ok) throw new UnauthorizedException("You do not have permission to modify this issue");
    }
    public IssueResponse toResponse(Issue i) {
        return IssueResponse.builder()
               .id(i.getId()).issueKey(i.getProject().getProjectKey() + "-" + i.getIssueNumber())
               .issueNumber(i.getIssueNumber()).title(i.getTitle()).description(i.getDescription())
               .type(i.getType()).status(i.getStatus()).priority(i.getPriority())
               .reporter(userMapper.toResponse(i.getReporter())).assignee(userMapper.toResponse(i.getAssignee()))
               .projectId(i.getProject().getId()).projectName(i.getProject().getName())
               .projectKey(i.getProject().getProjectKey()).dueDate(i.getDueDate())
               .storyPoints(i.getStoryPoints()).commentCount(commentRepository.countByIssue(i))
               .createdAt(i.getCreatedAt()).updatedAt(i.getUpdatedAt()).build();
    }
}
