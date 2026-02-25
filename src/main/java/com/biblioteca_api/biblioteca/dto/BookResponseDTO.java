package com.biblioteca_api.biblioteca.dto;

import com.biblioteca_api.biblioteca.entities.Book;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BookResponseDTO(
        Long id,
        String title,
        String isbn,
        BigDecimal price,
        LocalDate publishedDate,
        Long authorId,
        String authorName) {
    public BookResponseDTO(Book book) {
        this(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPrice(),
                book.getPublishedDate(),
                book.getAuthor().getId(),
                book.getAuthor().getName());
    }

    public static BookResponseDTO fromEntity(Book book) {
        return new BookResponseDTO(book);
    }
}
