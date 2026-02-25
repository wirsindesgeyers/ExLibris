package com.biblioteca_api.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.biblioteca_api.biblioteca.dto.BookRequestDTO;
import com.biblioteca_api.biblioteca.dto.BookResponseDTO;
import com.biblioteca_api.biblioteca.dto.ReviewRequestDTO;
import com.biblioteca_api.biblioteca.entities.Book;
import com.biblioteca_api.biblioteca.entities.Review;
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

    // (GET) - Livro pelo ID.
    @Operation(summary = "Retorna o livro pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        BookResponseDTO response = new BookResponseDTO(book);
        return ResponseEntity.ok(response);
    }

    // (GET) - Todos os livros
    @GetMapping
    @Operation(summary = "Retorna todos os livros")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookResponseDTO> response = books.stream()
                .map(BookResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    // (POST) - Criar livro
    @Operation(summary = "Cria um livro")
    @PostMapping
    public ResponseEntity<BookResponseDTO> postBook(@RequestBody @Valid BookRequestDTO dto) {
        Book book = bookService.createBook(dto);

        BookResponseDTO response = new BookResponseDTO(book);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/id}")
                .buildAndExpand(book.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    // (PUT) - EDITAR LIVRO
    @Operation(summary = "Edita um livro (por completo)")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> editBook(@RequestBody @Valid BookRequestDTO dto, @PathVariable Long id) {
        Book book = bookService.updateBook(id, dto);

        BookResponseDTO response = new BookResponseDTO(book);

        return ResponseEntity.ok(response);
    }

    // (DELETE) - DELETAR LIVRO
    @Operation(summary = "Deleta um livro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // (PATCH) - ADICIOnAR UM AUTHOR AO LIVRO
    @Operation(summary = "Adiciona um livro a um autor existente.")
    @PatchMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookResponseDTO> addAuthor(@PathVariable Long bookId, @PathVariable Long authorId) {

        Book book = bookService.alterAuthor(authorId, bookId);
        BookResponseDTO bookDTO = new BookResponseDTO(book);
        return ResponseEntity.ok(bookDTO);

    }

    // (PATCH) - ADICIONA UMA REVIEW AO LIVRO
    @Operation(summary = "Adicionar uma review a um livro")
    @PatchMapping("/{bookId}/reviews")
    public ResponseEntity<ReviewResponseDTO> addReview(@PathVariable Long bookId,
            @RequestBody @Valid ReviewRequestDTO data, @RequestParam Long userId) {
        Review review = reviewService.createReview(data, bookId, userId);

        ReviewResponseDTO response = ReviewResponseDTO.fromEntity(review);

        return ResponseEntity.ok(response);

    }

    // (DELETE) - DELETA A SUA PRÓPRIA REVIEW DE UM LIVRO
    @Operation(summary = "Deletar a sua própria review de um livro")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteOwnReview(@PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.noContent().build();
    }
}
