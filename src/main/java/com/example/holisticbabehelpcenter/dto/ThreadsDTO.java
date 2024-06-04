package com.example.holisticbabehelpcenter.dto;

import com.example.holisticbabehelpcenter.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreadsDTO {
    private Long id;
    private String title;
    private User author;
    private List<Long> postIds;
    private Long categoryId;
}
