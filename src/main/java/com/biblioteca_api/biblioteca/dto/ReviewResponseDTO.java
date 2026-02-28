package com.biblioteca_api.biblioteca.dto;

import java.time.LocalDate;

import com.biblioteca_api.biblioteca.entities.Review;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewResponseDTO(

        @Schema(description = "Id da avaliação", example = "1")
        Long reviewId,

        @Schema(description = "Resumo da avaliação do livro", example = "Excelente leitura!")
        String title,

        @Schema(description = "Descrição detalhada da avaliação", example = "O livro possui uma narrativa envolvente e personagens bem construídos.")
        String description,

        @Schema(description = "Data em que a avaliação foi feita", example = "2024-02-28")
        LocalDate createdAt,

        @Schema(description = "Avaliação do livro de 0 a 5", example = "5")
        short rating,

        @Schema(description = "Nome do usuário que fez a avaliação", example = "João Silva")
        String userName,

        @Schema(description = "Título do livro avaliado", example = "The book is on the table")
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
