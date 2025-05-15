package com.manansala.socialmedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {

    @Query("SELECT p FROM SocialMedia p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :key, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :key, '%')) " +
            "OR LOWER(p.mediaUrl) LIKE LOWER(CONCAT('%', :key, '%'))")
    List<SocialMedia> searchPosts(@Param("key") String key);
}
