package com.example.holisticbabehelpcenter.service;

import com.example.holisticbabehelpcenter.model.*;
import com.example.holisticbabehelpcenter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    // Category methods
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
    public List<Post> getPostsByThreadId(Long threadId) {
        Threads thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));
        return thread.getPosts();
    }
    public List<Threads> getThreadsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return category.getThreads();
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Thread methods
    public Threads createThread(String title, Category category, User author) {
        Threads thread = new Threads();
        thread.setTitle(title);
        thread.setCategory(category);
        thread.setAuthor(author);
        return threadRepository.save(thread);
    }

    public List<Threads> getAllThreads() {
        return threadRepository.findAll();
    }

    public Threads getThreadById(Long id) {
        return threadRepository.findById(id).orElseThrow(() -> new RuntimeException("Thread not found"));
    }

    public Threads updateThread(Threads thread) {
        return threadRepository.save(thread);
    }

    public void deleteThread(Long id) {
        threadRepository.deleteById(id);
    }

    // Post methods
    public Post createPost(Threads thread, String content, User author) {
        Post post = new Post();
        post.setThreads(thread);
        post.setContent(content);
        post.setAuthor(author);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // Comment methods
    public Comment createComment(Post post, String content, User author) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
