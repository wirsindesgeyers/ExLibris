package com.biblioteca_api.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.biblioteca_api.biblioteca.dto.BookRequestDTO;
import com.biblioteca_api.biblioteca.dto.BookResponseDTO;
import com.biblioteca_api.biblioteca.dto.ReviewRequestDTO;
import com.biblioteca_api.biblioteca.dto.ReviewResponseDTO;
import com.biblioteca_api.biblioteca.service.BookService;
import com.biblioteca_api.biblioteca.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;

    @Operation(summary = "Retorna o livro pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    @Operation(summary = "Retorna todos os livros")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Cria um livro")
    @PostMapping
    public ResponseEntity<BookResponseDTO> postBook(@RequestBody @Valid BookRequestDTO dto) {
        BookResponseDTO response = bookService.createBook(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Edita um livro (por completo)")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> editBook(@RequestBody @Valid BookRequestDTO dto, @PathVariable Long id) {

        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    @Operation(summary = "Deleta um livro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adiciona um livro a um autor existente.")
    @PatchMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookResponseDTO> addAuthor(@PathVariable Long bookId, @PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.alterAuthor(authorId, bookId));
    }

    @Operation(summary = "Adicionar uma review a um livro")
    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<ReviewResponseDTO> addReview(
            @PathVariable Long bookId,
            @RequestBody @Valid ReviewRequestDTO data,
            @RequestParam Long userId) {

        return ResponseEntity.ok(reviewService.createReview(data, bookId, userId));
    }

    @Operation(summary = "Lista as reviews de um livro")
    @GetMapping("/{bookId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> listBookReviews(@PathVariable Long bookId) {
        List<ReviewResponseDTO> response = reviewService.listReviewsFromBook(bookId);

        return ResponseEntity.ok(response);

    }

}
