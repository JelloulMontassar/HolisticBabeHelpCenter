package com.example.holisticbabehelpcenter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Threads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private User author;
    @OneToMany(mappedBy = "threads", cascade = CascadeType.REMOVE)
    private List<Post> posts;
    @JsonIgnore
    @ManyToOne
    private Category category;

}
