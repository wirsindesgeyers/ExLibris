package com.biblioteca_api.biblioteca.dto;

import com.biblioteca_api.biblioteca.entities.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BookResponseDTO(

        @Schema(description = "Id do livro", example = "1") Long id,

        @Schema(description = "Título do livro", example = "The book is on the table") String title,

        @Schema(description = "Código ISBN do livro", example = "978-85-333-0227-3") String isbn,

        @Schema(description = "Preço do livro", example = "10.50") BigDecimal price,

        @Schema(description = "Data de publicação do livro (yyyy/MM/dd)", example = "1999/07/13") LocalDate publishedDate,

        @Schema(description = "Id do autor do livro", example = "1") Long authorId,

        @Schema(description = "Nome do autor do livro", example = "Gilbert Keith Chesterton") String authorName,

        @Schema(description = "Média de avaliações do livro", example = "4.5") Double averageRating

) {
    public BookResponseDTO(Book book) {
        this(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPrice(),
                book.getPublishedDate(),
                book.getAuthor().getId(),
                book.getAuthor().getName(),
                book.getAverageRating());
    }

    public static BookResponseDTO fromEntity(Book book) {
        return new BookResponseDTO(book);
    }
}
