package com.biblioteca_api.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.biblioteca_api.biblioteca.dto.AuthorRequestDTO;
import com.biblioteca_api.biblioteca.dto.AuthorResponseDTO;
import com.biblioteca_api.biblioteca.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@EnableMethodSecurity
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/author")
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Cria um autor")
    public ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody
    @Valid
    AuthorRequestDTO data) {
        AuthorResponseDTO response = authorService.createAuthor(data);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Busca os dados de um autor")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthor(@PathVariable
    Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Apaga um autor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable
    Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retorna todos os autores")
    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Editar um autor por completo")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> editAuthor(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            AuthorRequestDTO data) {

        return ResponseEntity.ok(authorService.editAuthor(data, id));
    }
}
