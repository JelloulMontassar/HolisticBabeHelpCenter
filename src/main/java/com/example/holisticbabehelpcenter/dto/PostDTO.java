package com.example.holisticbabehelpcenter.dto;

import com.example.holisticbabehelpcenter.model.Comment;
import com.example.holisticbabehelpcenter.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private User author;
    private Long threadId;
    private String content;
    private LocalDateTime createdAt;
    private List<Comment> comments;
}
