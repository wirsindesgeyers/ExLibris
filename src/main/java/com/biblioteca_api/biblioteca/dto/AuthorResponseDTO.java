
package com.biblioteca_api.biblioteca.dto;

import java.time.LocalDate;
import java.util.List;

import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.entities.Book;

public record AuthorResponseDTO(

        Long id,
        LocalDate birthDate,
        String name,
        List<BookInfoDTO> bookInfos

) {
    public AuthorResponseDTO(Author author) {
        this(author.getId(), author.getBirthdate(), author.getName(),
                author.getBooks() != null
                        ? author.getBooks()
                                .stream()
                                .map(BookInfoDTO::new).toList()
                        : List.of());
    }

    public static AuthorResponseDTO fromEntity(Author author) {
        return new AuthorResponseDTO(author);
    }
}

record BookInfoDTO(Long id, String title, LocalDate publishedDate) {
    public BookInfoDTO(Book book) {
        this(book.getId(), book.getTitle(), book.getPublishedDate());
    }
}
