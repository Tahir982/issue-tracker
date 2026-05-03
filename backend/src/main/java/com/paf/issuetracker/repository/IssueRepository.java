package com.paf.issuetracker.repository;

import com.paf.issuetracker.entity.Issue;
import com.paf.issuetracker.entity.Project;
import com.paf.issuetracker.entity.User;
import com.paf.issuetracker.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("SELECT i FROM Issue i WHERE i.project = :p ORDER BY i.createdAt DESC")
    List<Issue> findByProjectOrderByCreatedAtDesc(@Param("p") Project project);

    List<Issue> findByProject(Project project);
    List<Issue> findByAssignee(User assignee);
    List<Issue> findByReporter(User reporter);

    long countByProjectAndStatus(Project project, IssueStatus status);

    @Query("SELECT MAX(i.issueNumber) FROM Issue i WHERE i.project = :p")
    Integer findMaxIssueNumberByProject(@Param("p") Project project);
}
