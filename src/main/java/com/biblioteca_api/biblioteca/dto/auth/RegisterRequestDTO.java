package com.biblioteca_api.biblioteca.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @Size(max = 50)
        @NotBlank(message = "O nome é obrigatório")
        @Schema(description = "Nickname do usuário", example = "brvtaldestroyer123")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail deve ser válido")
        @Size(max = 120)
        @Schema(description = "E-mail do usuário", example = "email@email.com")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 120, message = "A senha deve ter no mínimo 8 caracteres")
        @Schema(description = "Senha do usuário", example = "teste123")
        String password,

        @Schema(description = "Perfil de acesso (Obrigatório apenas para cadastros feitos por Admin)",
                example = "LIBRARIAN",
                allowableValues = {"ADMIN", "LIBRARIAN", "READER"})
        String role

) {}