package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manansala") // ✅ Ensuring the prefix is included for all routes
@CrossOrigin(origins = "https://final-ui-iw0x.onrender.com") // ✅ Global CORS setting
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

    // Handle CORS preflight requests properly
    @RequestMapping(value = "/post", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handlePreflight() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com")
                .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    // Get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        List<SocialMedia> posts = socialMediaRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    // Create a new post
    @PostMapping("/post")
    public ResponseEntity<SocialMedia> addNewPost(@RequestBody SocialMedia post) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "*");

        SocialMedia savedPost = socialMediaRepository.save(post);
        return new ResponseEntity<>(savedPost, headers, HttpStatus.OK);
    }

    // Update a post by ID
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
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        if (!socialMediaRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Post not found");
        }

        socialMediaRepository.deleteById(id);
        return ResponseEntity.ok("Post with id " + id + " deleted.");
    }

    // Get a post by ID
    @GetMapping("/posts/{id}")
    public ResponseEntity<SocialMedia> getPostById(@PathVariable Long id) {
        Optional<SocialMedia> post = socialMediaRepository.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Search posts
    @GetMapping("/posts/search/{key}")
    public ResponseEntity<List<SocialMedia>> searchPosts(@PathVariable String key) {
        List<SocialMedia> posts = socialMediaRepository.searchPosts(key);
        return posts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(posts);
    }

    // Bulk upload posts
    @PostMapping("/bulk-posts")
    public ResponseEntity<String> addMultiplePosts(@RequestBody List<SocialMedia> posts) {
        if (posts.isEmpty()) {
            return ResponseEntity.badRequest().body("Post list is empty.");
        }
        socialMediaRepository.saveAll(posts);
        return ResponseEntity.ok("Successfully added " + posts.size() + " posts.");
    }
}
