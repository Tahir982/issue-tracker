package com.paf.issuetracker.controller;

import com.paf.issuetracker.dto.request.CreateCommentRequest;
import com.paf.issuetracker.dto.response.*;
import com.paf.issuetracker.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/issues/{issueId}/comments")  public ResponseEntity<ApiResponse<List<CommentResponse>>> get(@PathVariable Long issueId)                                               { return ResponseEntity.ok(ApiResponse.success("OK", commentService.getByIssue(issueId))); }
    @PostMapping("/api/issues/{issueId}/comments") public ResponseEntity<ApiResponse<CommentResponse>>       add(@PathVariable Long issueId, @Valid @RequestBody CreateCommentRequest req) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Added", commentService.add(issueId, req))); }
    @PutMapping("/api/comments/{id}")              public ResponseEntity<ApiResponse<CommentResponse>>       update(@PathVariable Long id, @Valid @RequestBody CreateCommentRequest req)    { return ResponseEntity.ok(ApiResponse.success("Updated", commentService.update(id, req))); }
    @DeleteMapping("/api/comments/{id}")           public ResponseEntity<ApiResponse<Void>>                  delete(@PathVariable Long id)                                                  { commentService.delete(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null)); }
}
