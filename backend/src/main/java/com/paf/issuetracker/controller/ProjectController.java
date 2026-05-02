package com.paf.issuetracker.controller;

import com.paf.issuetracker.dto.request.*;
import com.paf.issuetracker.dto.response.*;
import com.paf.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/projects") @RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping          public ResponseEntity<ApiResponse<List<ProjectResponse>>> all()                                                     { return ResponseEntity.ok(ApiResponse.success("OK", projectService.getAllProjects())); }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<ProjectResponse>>       byId(@PathVariable Long id)                               { return ResponseEntity.ok(ApiResponse.success("OK", projectService.getProjectById(id))); }
    @PostMapping         public ResponseEntity<ApiResponse<ProjectResponse>>       create(@Valid @RequestBody CreateProjectRequest req)        { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Created", projectService.createProject(req))); }
    @PutMapping("/{id}") public ResponseEntity<ApiResponse<ProjectResponse>>       update(@PathVariable Long id, @Valid @RequestBody UpdateProjectRequest req) { return ResponseEntity.ok(ApiResponse.success("Updated", projectService.updateProject(id, req))); }
    @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>>               delete(@PathVariable Long id)                              { projectService.deleteProject(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null)); }

    @GetMapping("/{id}/members")              public ResponseEntity<ApiResponse<List<UserResponse>>> members(@PathVariable Long id)                                            { return ResponseEntity.ok(ApiResponse.success("OK", projectService.getMembers(id))); }
    @PostMapping("/{id}/members")             public ResponseEntity<ApiResponse<ProjectResponse>>   addMember(@PathVariable Long id, @Valid @RequestBody AddMemberRequest req)   { return ResponseEntity.ok(ApiResponse.success("Added", projectService.addMember(id, req))); }
    @DeleteMapping("/{id}/members/{userId}")  public ResponseEntity<ApiResponse<ProjectResponse>>   removeMember(@PathVariable Long id, @PathVariable Long userId)               { return ResponseEntity.ok(ApiResponse.success("Removed", projectService.removeMember(id, userId))); }
}
