package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class SocialMediaController {

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @GetMapping
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        return ResponseEntity.ok(socialMediaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<SocialMedia> createPost(@RequestBody SocialMedia post) {
        return ResponseEntity.ok(socialMediaRepository.save(post));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<SocialMedia>> searchPosts(@PathVariable String keyword) {
        return ResponseEntity.ok(socialMediaRepository
            .findByTitleContainingOrDescriptionContaining(keyword, keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocialMedia> updatePost(
            @PathVariable Long id,
            @RequestBody SocialMedia updatedPost) {
        return socialMediaRepository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setDescription(updatedPost.getDescription());
                post.setMediaUrl(updatedPost.getMediaUrl());
                return ResponseEntity.ok(socialMediaRepository.save(post));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return socialMediaRepository.findById(id)
            .map(post -> {
                socialMediaRepository.delete(post);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
