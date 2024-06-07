package com.example.holisticbabehelpcenter.controller;

import com.example.holisticbabehelpcenter.dto.CategoryDTO;
import com.example.holisticbabehelpcenter.dto.CommentDTO;
import com.example.holisticbabehelpcenter.dto.PostDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public List<CategoryDTO> getCategories() {
        List<Category> categories = forumService.getAllCategories();
        return categories.stream()
                         .map(category -> modelMapper.map(category, CategoryDTO.class))
                         .collect(Collectors.toList());
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
    @GetMapping("/threads")
    public List<ThreadsDTO> getThreads() {
        List<Threads> threads = forumService.getAllThreads();
        return threads.stream()
                .map(thread -> modelMapper.map(thread, ThreadsDTO.class))
                .collect(Collectors.toList());
    }
    @PutMapping("/threads")
    public Threads updateThread(@RequestBody Threads thread) {
        return forumService.updateThread(thread);
    }
    @PutMapping("/threads/{id}/update")
    public ResponseEntity<ThreadsDTO> updateThreadTitle(@RequestBody Threads thread) {
        Threads thread1 = forumService.getThreadById(thread.getId());
        thread1.setTitle(thread.getTitle());
        Threads updatedThread = forumService.updateThread(thread1);
        return ResponseEntity.ok(modelMapper.map(updatedThread, ThreadsDTO.class));
    }

    @DeleteMapping("/threads/{id}")
    public void deleteThread(@PathVariable Long id) {
        forumService.deleteThread(id);
    }

    @PostMapping("/posts/{threadId}")
    public PostDTO createPost(@PathVariable Long threadId, @RequestBody PostDTO postDTO, Authentication authentication) {
        Threads thread = threadRepository.findById(threadId).orElseThrow(() -> new RuntimeException("Thread not found"));
        User author = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setAuthor(author);
        post.setThreads(thread);
        post.setContent(postDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        Post createdPost = forumService.createPost(thread, post.getContent(), author);
        return modelMapper.map(createdPost, PostDTO.class);
    }

    @GetMapping("/threads/{threadId}/posts")
    public List<PostDTO> getPostsByThreadId(@PathVariable Long threadId) {
        List<Post> posts = forumService.getPostsByThreadId(threadId);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }
    @GetMapping("/posts/{id}")
    public PostDTO getPost(@PathVariable Long id) {
        Post post = forumService.getPostById(id);
        return modelMapper.map(post, PostDTO.class);
    }

    @PutMapping("/posts")
    public Post updatePost(@RequestBody Post post) {
        return forumService.updatePost(post);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        forumService.deletePost(id);
    }

    @PostMapping("/posts/{postId}/comments")
    public Comment createComment(@PathVariable Long postId,@RequestBody  Comment comment, Authentication authentication) {
        System.out.println(comment.getContent());
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User author = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setContent(comment.getContent());
        return forumService.createComment(comment);
    }

    @GetMapping("/comments")
    public List<Comment> getComments() {
        return forumService.getAllComments();
    }
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = forumService.getCommentsByPostId(postId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
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
