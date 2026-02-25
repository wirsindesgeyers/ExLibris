package com.biblioteca_api.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblioteca_api.biblioteca.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    List<Review> findByBookId(Long bookId);
}
