package com.biblioteca_api.biblioteca.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(

        @NotBlank(message = "Senha anterior não pode ser vazia")
        @Size(min = 8, max = 120)
        @Schema(description = "Senha atual do usuário", example = "feijaorastejante144")
        String oldPassword,

        @NotBlank(message = "Senha atual não pode ser vazia")
        @Size(min = 8, max = 120)
        @Schema(description = "Nova senha que o usuário deseja colocar", example = "feijaovoador123")
        String newPassword) {
}
