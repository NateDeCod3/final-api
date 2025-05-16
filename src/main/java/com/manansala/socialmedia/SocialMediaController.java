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
    @GetMapping("/posts")
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");

        List<SocialMedia> posts = socialMediaRepository.findAll();
        return new ResponseEntity<>(posts, headers, HttpStatus.OK);
    }

    // Create a new post
    @PostMapping("/post")
    public ResponseEntity<SocialMedia> addNewPost(@RequestBody SocialMedia post) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");

        SocialMedia savedPost = socialMediaRepository.save(post);
        return new ResponseEntity<>(savedPost, headers, HttpStatus.OK);
    }

    // Update a post by ID
    @PutMapping("/posts/{id}")
    public ResponseEntity<String> editPost(@PathVariable Long id, @RequestBody SocialMedia updatedPost) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");

        Optional<SocialMedia> optionalPost = socialMediaRepository.findById(id);
        if (!optionalPost.isPresent()) {
            return new ResponseEntity<>("Post not found", headers, HttpStatus.BAD_REQUEST);
        }

        SocialMedia post = optionalPost.get();
        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        post.setMediaUrl(updatedPost.getMediaUrl());
        socialMediaRepository.save(post);

        return new ResponseEntity<>("Post with id " + id + " updated.", headers, HttpStatus.OK);
    }

    // Delete a post by ID
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");

        if (!socialMediaRepository.existsById(id)) {
            return new ResponseEntity<>("Post not found", headers, HttpStatus.BAD_REQUEST);
        }

        socialMediaRepository.deleteById(id);
        return new ResponseEntity<>("Post with id " + id + " deleted.", headers, HttpStatus.OK);
    }

    // Get a post by ID
    @GetMapping("/posts/{id}")
    public ResponseEntity<SocialMedia> getPostById(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");

        Optional<SocialMedia> post = socialMediaRepository.findById(id);
        return post.map(p -> new ResponseEntity<>(p, headers, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND));
    }

    // Search posts
    @GetMapping("/posts/search/{key}")
    public ResponseEntity<List<SocialMedia>> searchPosts(@PathVariable String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");

        List<SocialMedia> posts = socialMediaRepository.searchPosts(key);
        return posts.isEmpty() ? new ResponseEntity<>(posts, headers, HttpStatus.NOT_FOUND)
                               : new ResponseEntity<>(posts, headers, HttpStatus.OK);
    }

    // Bulk upload posts
    @PostMapping("/bulk-posts")
    public ResponseEntity<String> addMultiplePosts(@RequestBody List<SocialMedia> posts) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://final-ui-iw0x.onrender.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");

        if (posts.isEmpty()) {
            return new ResponseEntity<>("Post list is empty.", headers, HttpStatus.BAD_REQUEST);
        }
        socialMediaRepository.saveAll(posts);
        return new ResponseEntity<>("Successfully added " + posts.size() + " posts.", headers, HttpStatus.OK);
    }
}
