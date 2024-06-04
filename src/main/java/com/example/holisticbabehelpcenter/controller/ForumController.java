package com.example.holisticbabehelpcenter.controller;

import com.example.holisticbabehelpcenter.dto.CategoryDTO;
import com.example.holisticbabehelpcenter.dto.ThreadsDTO;
import com.example.holisticbabehelpcenter.model.*;
import com.example.holisticbabehelpcenter.repository.CategoryRepository;
import com.example.holisticbabehelpcenter.repository.PostRepository;
import com.example.holisticbabehelpcenter.repository.ThreadRepository;
import com.example.holisticbabehelpcenter.repository.UserRepository;
import com.example.holisticbabehelpcenter.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ModelMapper modelMapper ;
    @Autowired
    private CategoryRepository categoryRepository;
    @PostMapping("/categories")
    public Category createCategory(@RequestParam String name) {
        return forumService.createCategory(name);
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return forumService.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public CategoryDTO getCategory(@PathVariable Long id) {
        Category category = forumService.getCategoryById(id);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @PutMapping("/categories")
    public Category updateCategory(@RequestBody Category category) {
        return forumService.updateCategory(category);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        forumService.deleteCategory(id);
    }

    @PostMapping("/threads/{categoryId}")
    public ThreadsDTO createThread(@RequestBody Threads thread, @PathVariable Long categoryId , Authentication authentication) {
        User author = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        Category category = forumService.getCategoryById(categoryId);
        Threads threads =  forumService.createThread(thread.getTitle(), category, author);
        return modelMapper.map(threads, ThreadsDTO.class);
    }

    @GetMapping("/categories/{categoryId}/threads")
    public List<ThreadsDTO> getThreadsByCategoryId(@PathVariable Long categoryId) {
        List<Threads> threads = forumService.getThreadsByCategoryId(categoryId);
        return threads.stream()
                .map(thread -> modelMapper.map(thread, ThreadsDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/threads/{id}")
    public Threads getThread(@PathVariable Long id) {
        return forumService.getThreadById(id);
    }

    @PutMapping("/threads")
    public Threads updateThread(@RequestBody Threads thread) {
        return forumService.updateThread(thread);
    }

    @DeleteMapping("/threads/{id}")
    public void deleteThread(@PathVariable Long id) {
        forumService.deleteThread(id);
    }

    @PostMapping("/posts")
    public Post createPost(@RequestParam Long threadId, @RequestParam String content, Authentication authentication) {
        Threads thread = threadRepository.findById(threadId).orElseThrow(() -> new RuntimeException("Thread not found"));
        User author = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        return forumService.createPost(thread, content, author);
    }

    @GetMapping("/posts")
    public List<Post> getPosts() {
        return forumService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        return forumService.getPostById(id);
    }

    @PutMapping("/posts")
    public Post updatePost(@RequestBody Post post) {
        return forumService.updatePost(post);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        forumService.deletePost(id);
    }

    @PostMapping("/comments")
    public Comment createComment(@RequestParam Long postId, @RequestParam String content, Authentication authentication) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User author = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        return forumService.createComment(post, content, author);
    }

    @GetMapping("/comments")
    public List<Comment> getComments() {
        return forumService.getAllComments();
    }

    @GetMapping("/comments/{id}")
    public Comment getComment(@PathVariable Long id) {
        return forumService.getCommentById(id);
    }

    @PutMapping("/comments")
    public Comment updateComment(@RequestBody Comment comment) {
        return forumService.updateComment(comment);
    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id) {
        forumService.deleteComment(id);
    }
}
