package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manansala")
@CrossOrigin(origins = "https://final-ui-iw0x.onrender.com") // Global CORS for all endpoints
public class SocialMediaController {

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @Autowired
    private DataSource dataSource;

    // Test database connection endpoint
    @GetMapping("/test-db")
    public ResponseEntity<String> testDbConnection() {
        try (Connection conn = dataSource.getConnection()) {
            return ResponseEntity.ok("Database connection successful to: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database connection failed: " + e.getMessage());
        }
    }

    // Get all posts
    @CrossOrigin(origins = "https://final-ui-iw0x.onrender.com")
    @GetMapping("/posts")
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        List<SocialMedia> posts = socialMediaRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    // Create a new post
    @CrossOrigin(origins = "https://final-ui-iw0x.onrender.com")
    @PostMapping("/post")
    public ResponseEntity<SocialMedia> addNewPost(@RequestBody SocialMedia post) {
        SocialMedia savedPost = socialMediaRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    // Update a post by ID
    @CrossOrigin(origins = "https://final-ui-iw0x.onrender.com")
    @PutMapping("/posts/{id}")
    public ResponseEntity<String> editPost(@PathVariable Long id, @RequestBody SocialMedia updatedPost) {
        Optional<SocialMedia> optionalPost = socialMediaRepository.findById(id);
        if (!optionalPost.isPresent()) {
            return ResponseEntity.badRequest().body("Post not found");
        }

        SocialMedia post = optionalPost.get();
        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        post.setMediaUrl(updatedPost.getMediaUrl());
        socialMediaRepository.save(post);

        return ResponseEntity.ok("Post with id " + id + " updated.");
    }

    // Delete a post by ID
    @CrossOrigin(origins = "https://final-ui-iw0x.onrender.com")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        if (!socialMediaRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Post not found");
        }

        socialMediaRepository.deleteById(id);
        return ResponseEntity.ok("Post with id " + id + " deleted.");
    }

    // Get a post by ID
    @CrossOrigin(origins = "https://final-ui-iw0x.onrender.com")
    @GetMapping("/posts/{id}")
    public ResponseEntity<SocialMedia> getPostById(@PathVariable Long id) {
        Optional<SocialMedia> post = socialMediaRepository.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Search posts
    @CrossOrigin(origins = "https://final-ui-iw0x.onrender.com")
    @GetMapping("/posts/search/{key}")
    public ResponseEntity<List<SocialMedia>> searchPosts(@PathVariable String key) {
        List<SocialMedia> posts = socialMediaRepository.searchPosts(key);
        return posts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(posts);
    }

    // Bulk upload posts
    @CrossOrigin(origins = "https://final-ui-iw0x.onrender.com")
    @PostMapping("/bulk-posts")
    public ResponseEntity<String> addMultiplePosts(@RequestBody List<SocialMedia> posts) {
        if (posts.isEmpty()) {
            return ResponseEntity.badRequest().body("Post list is empty.");
        }
        socialMediaRepository.saveAll(posts);
        return ResponseEntity.ok("Successfully added " + posts.size() + " posts.");
    }
}
