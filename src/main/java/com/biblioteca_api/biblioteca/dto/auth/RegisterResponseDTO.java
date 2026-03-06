package com.biblioteca_api.biblioteca.dto.auth;

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
}
