package com.paf.issuetracker.service;

import com.paf.issuetracker.dto.request.CreateCommentRequest;
import com.paf.issuetracker.dto.response.CommentResponse;
import com.paf.issuetracker.entity.Comment;
import com.paf.issuetracker.entity.Issue;
import com.paf.issuetracker.exception.*;
import com.paf.issuetracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<CommentResponse> getByIssue(Long issueId) {
        var issue = findIssue(issueId);
        return commentRepository.findByIssueOrderByCreatedAtAsc(issue).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CommentResponse add(Long issueId, CreateCommentRequest req) {
        var c = Comment.builder().content(req.getContent())
               .issue(findIssue(issueId)).author(userService.getCurrentUser()).build();
        commentRepository.save(c);
        return toResponse(c);
    }

    public CommentResponse update(Long id, CreateCommentRequest req) {
        var c = find(id); assertAuthor(c);
        c.setContent(req.getContent()); c.setEdited(true);
        return toResponse(c);
    }

    public void delete(Long id) {
        var c = find(id);
        var cur = userService.getCurrentUser();
        if (!c.getAuthor().getId().equals(cur.getId()) && !cur.getRole().name().equals("ADMIN"))
            throw new UnauthorizedException("You can only delete your own comments");
        commentRepository.delete(c);
    }

    private Issue findIssue(Long id) {
        return issueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Issue", id));
    }
    private Comment find(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", id));
    }
    private void assertAuthor(Comment c) {
        if (!c.getAuthor().getId().equals(userService.getCurrentUser().getId()))
            throw new UnauthorizedException("You can only edit your own comments");
    }
    private CommentResponse toResponse(Comment c) {
        return CommentResponse.builder().id(c.getId()).content(c.getContent())
               .author(userMapper.toResponse(c.getAuthor())).edited(c.isEdited())
               .createdAt(c.getCreatedAt()).updatedAt(c.getUpdatedAt()).build();
    }
}
