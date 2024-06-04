package com.example.holisticbabehelpcenter.controller;

import com.example.holisticbabehelpcenter.model.*;
import com.example.holisticbabehelpcenter.repository.PostRepository;
import com.example.holisticbabehelpcenter.repository.ThreadRepository;
import com.example.holisticbabehelpcenter.repository.UserRepository;
import com.example.holisticbabehelpcenter.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {
    @Autowired
    private ForumService forumService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @PostMapping("/categories")
    public Category createCategory(@RequestParam String name) {
        return forumService.createCategory(name);
    }
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return forumService.getAllCategories();
    }
    @PutMapping("/categories")
    public Category updateCategory(@RequestBody Category category) {
        return forumService.updateCategory(category);
    }
    @PostMapping("/threads")
    public Threads createThread(@RequestParam String title, Authentication authentication) {
        User author = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        return forumService.createThread(title, author);
    }

    @PostMapping("/posts")
    public Post createPost(@RequestParam Long threadId, @RequestParam String content, @RequestParam Long authorId) {
        Threads thread = threadRepository.findById(threadId).orElseThrow(() -> new RuntimeException("Thread not found"));
        User author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("User not found"));
        return forumService.createPost(thread, content, author);
    }

    @PostMapping("/comments")
    public Comment createComment(@RequestParam Long postId, @RequestParam String content, @RequestParam Long authorId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("User not found"));
        return forumService.createComment(post, content, author);
    }

}

