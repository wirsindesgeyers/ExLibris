package com.biblioteca_api.biblioteca.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @Size(max = 120, message = "O e-mail pode possuir no máximo 120 caracteres")
        @Email(message = "O e-mail deve ser valido")
        @NotBlank(message = "O e-mail é obrigatório")
        @Schema(description = "E-mail do usuário", example = "email@email.com")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Schema(description = "Senha do usuário", example = "teste123")
        @Size(min = 8, max = 120, message = "A senha deve ter entre 8 e 120 caracteres.")
        String password) {
}
