package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class SocialMediaController {

    @Autowired
    private SocialMediaRepository repository;

    @GetMapping
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialMedia> getPostById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SocialMedia> createPost(@Valid @RequestBody SocialMedia post) {
        return ResponseEntity.ok(repository.save(post));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<SocialMedia>> searchPosts(@PathVariable String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return ResponseEntity.ok(repository
            .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(lowerCaseKeyword, lowerCaseKeyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocialMedia> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody SocialMedia updatedPost) {
        return repository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setDescription(updatedPost.getDescription());
                post.setMediaUrl(updatedPost.getMediaUrl());
                return ResponseEntity.ok(repository.save(post));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return repository.findById(id)
            .map(post -> {
                repository.delete(post);
                return ResponseEntity.noContent().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
