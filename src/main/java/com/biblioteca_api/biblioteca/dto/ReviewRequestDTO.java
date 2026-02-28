package com.biblioteca_api.biblioteca.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequestDTO(

        @Schema(description = "Avaliação do livro de 0 a 5", example = "5")
        @Max(value = 5, message = "Rating cannot be higher than 5 ")
        @Min(value = 0, message = "Rating cannot be less than 0")
        @NotNull
        short rating,

        @Schema(description = "Resumo da avaliação do livro", example = "Excelente leitura!")
        @Size(max = 35, message = "Maximum number of characters (35) exceeded")
        @NotBlank(message = "title cannot be blank")
        String title,

        @Schema(description = "Descrição detalhada da avaliação", example = "O livro possui uma narrativa envolvente e personagens bem construídos.")
        @Size(max = 300, message = "Maximum number of characters (300) exceeded")
        @NotBlank(message = "description cannot be blank")
        String description

) {
}
