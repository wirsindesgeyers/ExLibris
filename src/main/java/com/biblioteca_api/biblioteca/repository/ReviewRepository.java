package com.biblioteca_api.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblioteca_api.biblioteca.entities.Review;
import com.biblioteca_api.biblioteca.entities.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserAndBookId(User user, Long bookId);

    boolean existsByIdAndUserEmail(Long reviewId, String userEmail);

    List<Review> findByBookId(Long bookId);

    List<Review> findByUserId(Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double getAverageRatingByBookId(@Param("bookId")
    Long bookId);
}
