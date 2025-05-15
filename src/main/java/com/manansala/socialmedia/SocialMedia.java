package com.manansala.socialmedia;

import jakarta.persistence.*;

@Entity
public class SocialMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social_media_seq")
    @SequenceGenerator(name = "social_media_seq", sequenceName = "social_media_seq", allocationSize = 1)
    private Long id;

    private String title;
    private String description;
    private String mediaUrl;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
}
