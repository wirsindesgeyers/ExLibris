package com.biblioteca_api.biblioteca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequestDTO(

        @Max(value = 5, message = "Rating cannot be higher than 5 ") @Min(value = 0, message = "Rating cannot be less than 0") @NotNull short rating,

        @Size(max = 35, message = "Maximum number of characters (35) exceeded") @NotBlank(message = "title cannot be blank") String title,

        @Size(max = 300, message = "Maximum number of characters (300) exceeded") @NotBlank(message = "description cannot be blank") String description

) {
}
