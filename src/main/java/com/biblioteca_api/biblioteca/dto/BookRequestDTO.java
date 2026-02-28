package com.biblioteca_api.biblioteca.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.ISBN;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookRequestDTO(

        @Schema(description = "Título do livro", example = "The book is on the table'")
        @NotBlank(message = "O livro precisa ter um titulo.")
        @Size(max = 150, message = "Tamanho de caracteres excedido")
        String title,

        @Schema(description = "Código ISBN do livro", example = "ISBN 978-85-333-0227-3")
        @ISBN
        @Size(max = 13)
        @NotNull(message = "isbn é obrigatorio")
        String isbn,

        @Schema(description = "Preço do livro", example = "10.50")
        @Min(value = 0, message = "O valor não pode ser menor que 0.")
        @NotNull(message = "O livro precisa ter um preço")
        BigDecimal price,

        @Schema(description = "Data de publicação do livro (yyyy/MM/dd", example = "1999/07/13")
        @NotNull(message = "O livro precisa de data de publicação")
        @PastOrPresent(message = "O livro não pode ser publicado no futuro")
        LocalDate publishedDate,

        @Schema(description = "Id do autor do livro", example = "1")
        @NotNull
        Long authorId

) {
}
