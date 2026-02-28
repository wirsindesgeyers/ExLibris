package com.biblioteca_api.biblioteca.dto;

import java.util.List;
import com.biblioteca_api.biblioteca.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDTO(
        @Schema(description = "Id do usuário", example = "1")
        Long id,

        @Schema(description = "Nome do usuário", example = "Bruno")
        String name,

        @Schema(description = "Email do usuário", example = "bruno@example.com")
        String email,

        @Schema(description = "Lista de breves informações das avaliações do usuário")
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

// Record auxiliar para mostrar apenas o essencial das reviews do usuário
record UserReviewInfoDTO(
        @Schema(description = "Id da avaliação", example = "1")
        Long id,

        @Schema(description = "Título da avaliação", example = "Gostei muito!")
        String title,

        @Schema(description = "Nota dada pelo usuário", example = "5")
        short rating,

        @Schema(description = "Título do livro avaliado", example = "The book is on the table")
        String bookTitle) {
    public UserReviewInfoDTO(com.biblioteca_api.biblioteca.entities.Review review) {
        this(
                review.getId(),
                review.getTitle(),
                review.getRating(),
                review.getBook().getTitle());
    }
}
