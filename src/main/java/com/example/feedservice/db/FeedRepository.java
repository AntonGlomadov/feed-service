package com.example.feedservice.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    List<FeedEntity> findByUsernameIn(List<String> usernames);
    FeedEntity findFeedEntityById(Long id);
    List<FeedEntity> findAllByUsername(String username);
}
