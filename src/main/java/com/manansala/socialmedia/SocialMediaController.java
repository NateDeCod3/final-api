package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manansala")
@CrossOrigin(origins = "http://localhost:5173")
public class SocialMediaController {

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    // Get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        List<SocialMedia> posts = socialMediaRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    // Create a new post
    @PostMapping("/post")
    public ResponseEntity<SocialMedia> addNewPost(@RequestBody SocialMedia post) {
        SocialMedia savedPost = socialMediaRepository.save(post);
        return ResponseEntity.ok(savedPost);
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

    // Search posts by title, description, or media URL
    @GetMapping("/posts/search/{key}")
    public ResponseEntity<List<SocialMedia>> searchPosts(@PathVariable String key) {
        List<SocialMedia> posts = socialMediaRepository.searchPosts(key);

        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(posts);
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
