package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.request.*;
import com.paf.issuetracker.dto.response.*;
import com.paf.issuetracker.entity.Project;
import com.paf.issuetracker.entity.User;
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
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAllAccessibleByUser(userService.getCurrentUser())
               .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) { return toResponse(find(id)); }

    public ProjectResponse createProject(CreateProjectRequest req) {
        if (projectRepository.existsByProjectKey(req.getProjectKey()))
            throw new DuplicateResourceException("Project key already exists: " + req.getProjectKey());
        var owner = userService.getCurrentUser();
        var p = Project.builder().name(req.getName()).description(req.getDescription())
               .projectKey(req.getProjectKey().toUpperCase()).owner(owner).build();
        p.getMembers().add(owner);
        projectRepository.save(p);
        return toResponse(p);
    }

    public ProjectResponse updateProject(Long id, UpdateProjectRequest req) {
        var p = find(id); assertOwnerOrAdmin(p);
        if (req.getName()        != null) p.setName(req.getName());
        if (req.getDescription() != null) p.setDescription(req.getDescription());
        if (req.getArchived()    != null) p.setArchived(req.getArchived());
        return toResponse(p);
    }

    public void deleteProject(Long id) { var p = find(id); assertOwnerOrAdmin(p); projectRepository.delete(p); }

    public ProjectResponse addMember(Long pid, AddMemberRequest req) {
        var p = find(pid); assertOwnerOrAdmin(p);
        var u = userRepository.findById(req.getUserId())
               .orElseThrow(() -> new ResourceNotFoundException("User", req.getUserId()));
        p.getMembers().add(u);
        return toResponse(p);
    }

    public ProjectResponse removeMember(Long pid, Long uid) {
        var p = find(pid); assertOwnerOrAdmin(p);
        if (p.getOwner().getId().equals(uid)) throw new UnauthorizedException("Cannot remove the project owner");
        var u = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User", uid));
        p.getMembers().remove(u);
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getMembers(Long pid) {
        return find(pid).getMembers().stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    // ── helpers ──────────────────────────────────────────
    private Project find(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }

    private void assertOwnerOrAdmin(Project p) {
        var cur = userService.getCurrentUser();
        boolean ok = p.getOwner().getId().equals(cur.getId()) || cur.getRole().name().equals("ADMIN");
        if (!ok) throw new UnauthorizedException("Only the project owner or admin can do this");
    }

    private ProjectResponse toResponse(Project p) {
        long total = issueRepository.findByProject(p).size();
        long open  = issueRepository.countByProjectAndStatus(p, IssueStatus.OPEN);
        return ProjectResponse.builder()
               .id(p.getId()).name(p.getName()).description(p.getDescription())
               .projectKey(p.getProjectKey()).owner(userMapper.toResponse(p.getOwner()))
               .members(p.getMembers().stream().map(userMapper::toResponse).collect(Collectors.toList()))
               .archived(p.isArchived()).totalIssues(total).openIssues(open)
               .createdAt(p.getCreatedAt()).updatedAt(p.getUpdatedAt()).build();
    }
}
