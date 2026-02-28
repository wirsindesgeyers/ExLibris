package com.biblioteca_api.biblioteca.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record AuthorRequestDTO(

        @Schema(description = "Nome completo do autor do livro", example = "Gilbert Keith Chesterton")
        @NotBlank(message = "Autor deve ter um nome.")
        @Size(max = 100, message = "Tamanho máximo de caracteres (100) foi excedido.")
        String name,

        @Schema(description = "Data de nascimento do autor do livro (yyyy/MM/dd)", example = "1874/05/29")
        @NotNull(message = "Data de nascimento não pode ser nula")
        @PastOrPresent(message = "Nascimento do autor deve ser no passado.")
        LocalDate birthDate

) {
}
