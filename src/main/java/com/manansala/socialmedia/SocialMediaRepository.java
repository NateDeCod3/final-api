package com.manansala.socialmedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {
    List<SocialMedia> findByTitleOrDescription(String title, String description);
}
