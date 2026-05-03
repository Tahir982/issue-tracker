package com.paf.issuetracker.repository;

import com.paf.issuetracker.entity.Comment;
import com.paf.issuetracker.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByIssueOrderByCreatedAtAsc(Issue issue);
    long countByIssue(Issue issue);
}
