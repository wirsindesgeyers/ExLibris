
package com.biblioteca_api.biblioteca.dto;

import java.time.LocalDate;
import java.util.List;

import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.entities.Book;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthorResponseDTO(

        @Schema(description = "Id do autor ", example = "2328")
        Long id,

        @Schema(description = "Data de Nascimento do Autor (yyyy/MM/dd)", example = "1874/05/29")
        LocalDate birthDate,

        @Schema(description = "Nome do Autor de livros", example = "Gilbert Keith Chesterton")
        String name,

        @Schema(description = "Lista de breves informações dos livros do autor", example = "Id, titulo e data de publicação")
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

record BookInfoDTO(

        @Schema(description = "Id do livro")
        Long id,

        @Schema(description = "Título do livro")
        String title,

        @Schema(description = "Data de publicação do livro")
        LocalDate publishedDate

) {
    public BookInfoDTO(Book book) {
        this(book.getId(), book.getTitle(), book.getPublishedDate());
    }
}
