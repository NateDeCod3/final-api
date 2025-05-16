package com.manansala.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manansala")
public class SocialMediaController {

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @GetMapping("/posts")
    public ResponseEntity<List<SocialMedia>> getAllPosts() {
        return ResponseEntity.ok(socialMediaRepository.findAll());
    }

    @PostMapping("/posts")
    public ResponseEntity<SocialMedia> addNewPost(@RequestBody SocialMedia post) {
        return ResponseEntity.ok(socialMediaRepository.save(post));
    }

    @PostMapping("/bulk-posts")
    public ResponseEntity<List<SocialMedia>> addBulkPosts(@RequestBody List<SocialMedia> posts) {
        return ResponseEntity.ok(socialMediaRepository.saveAll(posts));
    }

    @GetMapping("/posts/search/{key}")
    public ResponseEntity<List<SocialMedia>> searchPosts(@PathVariable String key) {
        return ResponseEntity.ok(socialMediaRepository
            .findByTitleContainingOrDescriptionContaining(key, key));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<SocialMedia> editPost(@PathVariable Long id, @RequestBody SocialMedia updatedPost) {
        return socialMediaRepository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setDescription(updatedPost.getDescription());
                post.setMediaUrl(updatedPost.getMediaUrl());
                return ResponseEntity.ok(socialMediaRepository.save(post));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return socialMediaRepository.findById(id)
            .map(post -> {
                socialMediaRepository.delete(post);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
