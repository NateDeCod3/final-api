package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/posts")
public class SocialMediaController {

    @Autowired
    private SocialMediaRepository repository;

    @GetMapping
    public ResponseEntity<Page<SocialMedia>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(repository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialMedia> getPostById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SocialMedia> createPost(@Valid @RequestBody SocialMedia post) {
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(post));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<SocialMedia>> searchPosts(
            @PathVariable String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(repository
            .findByTitleContainingOrDescriptionContaining(keyword, keyword, pageable));
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
                post.setUpdatedAt(LocalDateTime.now());
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
