package com.biblioteca_api.biblioteca.dto.auth;

import com.biblioteca_api.biblioteca.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponseDTO(
        @Schema(description = "ID único do usuário no banco", example = "1")
        Long id,

        @Schema(description = "E-mail cadastrado", example = "kauan@exlibris.com")
        String email,

        @Schema(description = "Perfil de acesso atribuído", example = "READER")
        String role,

        @Schema(description = "Mensagem de confirmação", example = "Usuário registrado com sucesso!")
        String message) {


    public RegisterResponseDTO(User user){
        this(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                "Usuário cadastrado com sucesso!"
        );
    }

    public static RegisterResponseDTO fromEntity(User user) {
        return new RegisterResponseDTO(user);
        }
}
