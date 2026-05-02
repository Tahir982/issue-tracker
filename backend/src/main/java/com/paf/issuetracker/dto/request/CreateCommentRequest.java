package com.paf.issuetracker.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotBlank(message="Comment cannot be blank")
    @Size(min=1, max=5000)
    private String content;
}
