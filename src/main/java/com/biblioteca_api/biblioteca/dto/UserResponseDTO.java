package com.biblioteca_api.biblioteca.dto;

import java.util.List;
import com.biblioteca_api.biblioteca.entities.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        List<UserReviewInfoDTO> reviews) {
    // Constructor mapping
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getReviews() != null
                        ? user.getReviews().stream().map(UserReviewInfoDTO::new).toList()
                        : List.of());
    }

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(user);
    }
}

/**
 * Record auxiliar para mostrar apenas o essencial das reviews do usuário
 */
record UserReviewInfoDTO(
        Long id,
        String title,
        short rating,
        String bookTitle) {
    public UserReviewInfoDTO(com.biblioteca_api.biblioteca.entities.Review review) {
        this(
                review.getId(),
                review.getTitle(),
                review.getRating(),
                review.getBook().getTitle());
    }
}
