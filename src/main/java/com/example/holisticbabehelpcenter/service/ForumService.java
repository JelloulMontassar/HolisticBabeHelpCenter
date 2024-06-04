package com.example.holisticbabehelpcenter.service;
import com.example.holisticbabehelpcenter.model.*;
import com.example.holisticbabehelpcenter.repository.CategoryRepository;
import com.example.holisticbabehelpcenter.repository.CommentRepository;
import com.example.holisticbabehelpcenter.repository.PostRepository;
import com.example.holisticbabehelpcenter.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ForumService {
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    public Threads createThread(String title, User author) {
        Threads thread = new Threads();
        thread.setTitle(title);
        thread.setAuthor(author);
        return threadRepository.save(thread);
    }
    public Post createPost(Threads thread, String content, User author) {
        Post post = new Post();
        post.setThreads(thread);
        post.setContent(content);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
    public Comment createComment(Post post, String content, User author) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}

