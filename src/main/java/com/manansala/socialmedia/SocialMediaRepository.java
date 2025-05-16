package com.manansala.socialmedia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {
    Page<SocialMedia> findByTitleContainingOrDescriptionContaining(
        String titleKeyword, 
        String descriptionKeyword, 
        Pageable pageable);
}
