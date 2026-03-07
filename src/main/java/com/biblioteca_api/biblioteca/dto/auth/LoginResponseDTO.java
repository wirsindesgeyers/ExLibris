package com.biblioteca_api.biblioteca.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
        @Schema(description = "Token de autenticação JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tipo do token", example = "Bearer")
        String type,

        @Schema(description = "Papel do usuário no sistema", example = "READER")
        String role) {

}
