package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.request.CreateProjectRequest;
import com.paf.issuetracker.dto.response.ProjectResponse;
import com.paf.issuetracker.entity.*;
import com.paf.issuetracker.enums.Role;
import com.paf.issuetracker.exception.DuplicateResourceException;
import com.paf.issuetracker.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock ProjectRepository projectRepository;
    @Mock UserRepository userRepository;
    @Mock IssueRepository issueRepository;
    @Mock UserService userService;
    @Mock UserMapper userMapper;
    @InjectMocks ProjectService projectService;

    User pm;

    @BeforeEach void setUp() {
        pm = User.builder().id(1L).username("pm").fullName("PM").role(Role.PROJECT_MANAGER).active(true).build();
    }

    @Test void createProject_success() {
        var req = new CreateProjectRequest();
        req.setName("New System"); req.setProjectKey("NS"); req.setDescription("desc");
        when(projectRepository.existsByProjectKey("NS")).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(pm);
        when(projectRepository.save(any())).thenAnswer(inv -> { Project p = inv.getArgument(0); p.setId(1L); return p; });
        when(issueRepository.findByProject(any())).thenReturn(List.of());
        when(issueRepository.countByProjectAndStatus(any(), any())).thenReturn(0L);

        ProjectResponse res = projectService.createProject(req);
        assertThat(res.getName()).isEqualTo("New System");
        assertThat(res.getProjectKey()).isEqualTo("NS");
        verify(projectRepository).save(any());
    }

    @Test void createProject_duplicateKey_throws() {
        var req = new CreateProjectRequest();
        req.setName("X"); req.setProjectKey("DUP");
        when(projectRepository.existsByProjectKey("DUP")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> projectService.createProject(req));
        verify(projectRepository, never()).save(any());
    }
}
