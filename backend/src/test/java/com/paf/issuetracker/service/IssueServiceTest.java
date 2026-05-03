package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.request.*;
import com.paf.issuetracker.dto.response.IssueResponse;
import com.paf.issuetracker.entity.*;
import com.paf.issuetracker.enums.*;
import com.paf.issuetracker.exception.ResourceNotFoundException;
import com.paf.issuetracker.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock IssueRepository issueRepository;
    @Mock ProjectRepository projectRepository;
    @Mock UserRepository userRepository;
    @Mock CommentRepository commentRepository;
    @Mock UserService userService;
    @Mock UserMapper userMapper;
    @InjectMocks IssueService issueService;

    User dev; Project proj;

    @BeforeEach void setUp() {
        dev  = User.builder().id(1L).username("dev").email("d@t.com").fullName("Dev").role(Role.DEVELOPER).active(true).build();
        proj = Project.builder().id(1L).name("Proj").projectKey("PRJ").owner(dev).build();
    }

    @Test void createIssue_firstInProject_getsNumberOne() {
        var req = new CreateIssueRequest();
        req.setTitle("First issue ever"); req.setType(IssueType.BUG); req.setPriority(IssuePriority.HIGH);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(proj));
        when(userService.getCurrentUser()).thenReturn(dev);
        when(issueRepository.findMaxIssueNumberByProject(proj)).thenReturn(null);
        when(issueRepository.save(any())).thenAnswer(inv -> { Issue i = inv.getArgument(0); i.setId(1L); return i; });
        when(commentRepository.countByIssue(any())).thenReturn(0L);

        var res = issueService.create(1L, req);
        assertThat(res.getIssueNumber()).isEqualTo(1);
        assertThat(res.getStatus()).isEqualTo(IssueStatus.OPEN);
    }

    @Test void createIssue_incrementsIssueNumber() {
        var req = new CreateIssueRequest();
        req.setTitle("Another bug here"); req.setType(IssueType.BUG); req.setPriority(IssuePriority.MEDIUM);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(proj));
        when(userService.getCurrentUser()).thenReturn(dev);
        when(issueRepository.findMaxIssueNumberByProject(proj)).thenReturn(7);
        when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(commentRepository.countByIssue(any())).thenReturn(0L);

        var res = issueService.create(1L, req);
        assertThat(res.getIssueNumber()).isEqualTo(8);
    }

    @Test void createIssue_projectNotFound_throws() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        var req = new CreateIssueRequest();
        req.setTitle("test title here"); req.setType(IssueType.TASK); req.setPriority(IssuePriority.LOW);
        assertThrows(ResourceNotFoundException.class, () -> issueService.create(99L, req));
    }

    @Test void updateStatus_changesStatus() {
        var issue = Issue.builder().id(1L).title("Bug").status(IssueStatus.OPEN)
               .type(IssueType.BUG).priority(IssuePriority.HIGH)
               .project(proj).reporter(dev).issueNumber(1).build();
        var req = new UpdateIssueStatusRequest(); req.setStatus(IssueStatus.IN_PROGRESS);
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(commentRepository.countByIssue(any())).thenReturn(0L);

        var res = issueService.updateStatus(1L, req);
        assertThat(res.getStatus()).isEqualTo(IssueStatus.IN_PROGRESS);
    }

    @Test void getById_notFound_throws() {
        when(issueRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> issueService.getById(404L));
    }
}
