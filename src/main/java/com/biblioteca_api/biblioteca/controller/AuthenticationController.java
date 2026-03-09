package com.biblioteca_api.biblioteca.controller;

import com.biblioteca_api.biblioteca.dto.auth.RegisterRequestDTO;
import com.biblioteca_api.biblioteca.entities.UserRole;
import com.biblioteca_api.biblioteca.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.biblioteca_api.biblioteca.dto.auth.LoginRequestDTO;
import com.biblioteca_api.biblioteca.dto.auth.LoginResponseDTO;
import com.biblioteca_api.biblioteca.dto.auth.RegisterResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @Operation(summary = "Registra um usuário (sempre será READER)")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody
    @Valid
    RegisterRequestDTO data) {

        var response = authService.register(data, UserRole.READER);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Admin registra novos usuários escolhendo a Role")
    @PostMapping("/admin/register-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponseDTO> registerByAdmin(@RequestBody
    @Valid
    RegisterRequestDTO data) {

        if (data.role() == null || data.role().isBlank()) {
            throw new RuntimeException("Admin, você precisa informar a Role do novo usuário!");
        }

        UserRole roleFromDTO = UserRole.valueOf(data.role().toUpperCase());
        var response = authService.register(data, roleFromDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Loga um usuário")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody
    @Valid
    LoginRequestDTO data) {

        LoginResponseDTO response = authService.login(data);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
