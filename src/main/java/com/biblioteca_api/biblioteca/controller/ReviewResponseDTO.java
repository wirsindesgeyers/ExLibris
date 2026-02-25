package com.biblioteca_api.biblioteca.controller;

import java.time.LocalDate;

import com.biblioteca_api.biblioteca.entities.Review;

public record ReviewResponseDTO(

        Long reviewId,
        String title,
        String description,
        LocalDate createdAt,
        short rating,
        String userName,
        String bookTitle

) {
    public static ReviewResponseDTO fromEntity(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getTitle(),
                review.getDescription(),
                review.getCreatedAt(),
                review.getRating(),
                review.getUser().getName(),
                review.getBook().getTitle());
    }

}
