package com.paf.issuetracker.controller;

import com.paf.issuetracker.dto.request.*;
import com.paf.issuetracker.dto.response.*;
import com.paf.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/api/projects/{pid}/issues")  public ResponseEntity<ApiResponse<List<IssueResponse>>> byProject(@PathVariable Long pid)                                                    { return ResponseEntity.ok(ApiResponse.success("OK", issueService.getByProject(pid))); }
    @PostMapping("/api/projects/{pid}/issues") public ResponseEntity<ApiResponse<IssueResponse>>       create(@PathVariable Long pid, @Valid @RequestBody CreateIssueRequest req)            { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Created", issueService.create(pid, req))); }
    @GetMapping("/api/issues/{id}")            public ResponseEntity<ApiResponse<IssueResponse>>       byId(@PathVariable Long id)                                                           { return ResponseEntity.ok(ApiResponse.success("OK", issueService.getById(id))); }
    @PutMapping("/api/issues/{id}")            public ResponseEntity<ApiResponse<IssueResponse>>       update(@PathVariable Long id, @Valid @RequestBody UpdateIssueRequest req)             { return ResponseEntity.ok(ApiResponse.success("Updated", issueService.update(id, req))); }
    @PatchMapping("/api/issues/{id}/status")   public ResponseEntity<ApiResponse<IssueResponse>>       status(@PathVariable Long id, @Valid @RequestBody UpdateIssueStatusRequest req)       { return ResponseEntity.ok(ApiResponse.success("Updated", issueService.updateStatus(id, req))); }
    @PatchMapping("/api/issues/{id}/assign")   public ResponseEntity<ApiResponse<IssueResponse>>       assign(@PathVariable Long id, @RequestBody AssignIssueRequest req)                   { return ResponseEntity.ok(ApiResponse.success("Assigned", issueService.assign(id, req))); }
    @DeleteMapping("/api/issues/{id}")         public ResponseEntity<ApiResponse<Void>>                delete(@PathVariable Long id)                                                         { issueService.delete(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null)); }
    @GetMapping("/api/issues/my-issues")       public ResponseEntity<ApiResponse<List<IssueResponse>>> mine()                                                                                { return ResponseEntity.ok(ApiResponse.success("OK", issueService.myAssigned())); }
}
