package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manansala") // ✅ Ensuring `/manansala` is applied to all routes
@CrossOrigin(origins = "https://final-ui-iw0x.onrender.com") // ✅ Global CORS setting
public class SocialMediaController {

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    // Get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        return ResponseEntity.ok(socialMediaRepository.findAll());
    }

    // Create a new post
    @PostMapping("/post")
    public ResponseEntity<SocialMedia> addNewPost(@RequestBody SocialMedia post) {
        return ResponseEntity.ok(socialMediaRepository.save(post));
    }

    // Update a post
    @PutMapping("/posts/{id}")
    public ResponseEntity<String> editPost(@PathVariable Long id, @RequestBody SocialMedia updatedPost) {
        Optional<SocialMedia> post = socialMediaRepository.findById(id);
        if (!post.isPresent()) {
            return ResponseEntity.badRequest().body("Post not found");
        }

        post.get().setTitle(updatedPost.getTitle());
        post.get().setDescription(updatedPost.getDescription());
        post.get().setMediaUrl(updatedPost.getMediaUrl());
        socialMediaRepository.save(post.get());

        return ResponseEntity.ok("Post updated successfully!");
    }

    // Delete a post
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        if (!socialMediaRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Post not found");
        }
        socialMediaRepository.deleteById(id);
        return ResponseEntity.ok("Post deleted successfully!");
    }
}
