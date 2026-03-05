package com.biblioteca_api.biblioteca.service;

import com.biblioteca_api.biblioteca.dto.BookRequestDTO;
import com.biblioteca_api.biblioteca.dto.BookResponseDTO;
import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.entities.Book;
import com.biblioteca_api.biblioteca.factories.AuthorFactory;
import com.biblioteca_api.biblioteca.factories.BookFactory;
import com.biblioteca_api.biblioteca.infra.exceptions.BookAlreadyExistsException;
import com.biblioteca_api.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private Author author;
    private BookRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        author = AuthorFactory.createValidAuthor();
        book = BookFactory.createValidBook(author);
        requestDTO = new BookRequestDTO("O Hobbit", "0987654321", new BigDecimal("100.00"), LocalDate.of(1937, 9, 21),
                1L);
    }

    // TESTES PARA findBookEntity e validateBookExists

    @Test
    @DisplayName("Deve retornar a entidade Book quando o ID existir")
    void findBookEntity_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.findBookEntity(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException 404 quando buscar entidade de livro inexistente")
    void findBookEntity_ThrowsNotFound() {
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookService.findBookEntity(100L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException 404 quando validar existência de livro que não existe")
    void validateBookExists_ThrowsNotFound() {
        when(bookRepository.existsById(100L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookService.validateBookExists(100L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Não deve lançar exceção quando validar existência de livro que existe")
    void validateBookExists_Success() {
        when(bookRepository.existsById(100L)).thenReturn(true);
        assertDoesNotThrow(() -> bookService.validateBookExists(100L));
    }

    // TESTES PARA getBookById e getAllBooks

    @Test
    @DisplayName("Deve retornar BookResponseDTO com sucesso")
    void getBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponseDTO response = bookService.getBookById(1L);

        assertNotNull(response);
        assertEquals(book.getTitle(), response.title());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException 404 quando buscar livro inexistente pelo ID")
    void getBookById_ThrowsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookService.getBookById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver livros")
    void getAllBooks_ReturnsEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookResponseDTO> response = bookService.getAllBooks();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista de BookResponseDTO")
    void getAllBooks_ReturnsList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponseDTO> response = bookService.getAllBooks();

        assertEquals(1, response.size());
    }

    // --- TESTES PARA createBook ---

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    void createBook_Success() {
        when(bookRepository.existsByIsbn(requestDTO.isbn())).thenReturn(false);
        when(authorService.findAuthorEntity(requestDTO.authorId())).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO response = bookService.createBook(requestDTO);

        assertNotNull(response);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Deve lançar BookAlreadyExistsException se ISBN já existir na criação")
    void createBook_ThrowsBookAlreadyExistsException() {
        when(bookRepository.existsByIsbn(requestDTO.isbn())).thenReturn(true);

        assertThrows(BookAlreadyExistsException.class, () -> bookService.createBook(requestDTO));
        verify(bookRepository, never()).save(any(Book.class));
    }

    // --- TESTES PARA deleteBookById ---

    @Test
    @DisplayName("Deve deletar livro com sucesso")
    void deleteBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBookById(1L);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException 404 ao tentar deletar livro inexistente")
    void deleteBookById_ThrowsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> bookService.deleteBookById(99L));

        verify(bookRepository, never()).delete(any());
    }

    // --- TESTES PARA updateBook ---

    @Test
    @DisplayName("Deve atualizar o livro com sucesso sem alterar o autor")
    void updateBook_Success_SameAuthor() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // requestDTO possui o mesmo authorId (1L) que o livro já possui
        bookService.updateBook(1L, requestDTO);

        verify(authorService, never()).findAuthorEntity(anyLong());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Deve atualizar o livro e alterar o autor caso o ID venha diferente")
    void updateBook_Success_DifferentAuthor() {
        BookRequestDTO updateRequest = new BookRequestDTO("Novo Titulo", "1111", new BigDecimal("50.00"),
                LocalDate.now(), 2L);
        Author newAuthor = new Author();
        newAuthor.setId(2L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorService.findAuthorEntity(2L)).thenReturn(newAuthor);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.updateBook(1L, updateRequest);

        verify(authorService, times(1)).findAuthorEntity(2L);
        assertEquals(2L, book.getAuthor().getId());
    }

    @Test
    @DisplayName("Deve buscar autor quando book.getAuthor() for null no update")
    void updateBook_Success_NullAuthorOnBook() {
        book.setAuthor(null); // simula livro sem autor associado

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorService.findAuthorEntity(1L)).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.updateBook(1L, requestDTO); // requestDTO tem authorId=1L

        verify(authorService, times(1)).findAuthorEntity(1L);
        assertEquals(1L, book.getAuthor().getId());
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException 404 ao tentar atualizar livro inexistente")
    void updateBook_ThrowsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> bookService.updateBook(99L, requestDTO));

        verify(bookRepository, never()).save(any());
    }

    // --- TESTES PARA alterAuthor ---

    @Test
    @DisplayName("Deve alterar apenas o autor do livro com sucesso")
    void alterAuthor_Success() {
        Author newAuthor = new Author();
        newAuthor.setId(2L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorService.findAuthorEntity(2L)).thenReturn(newAuthor);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.alterAuthor(2L, 1L);

        assertEquals(2L, book.getAuthor().getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException 404 ao alterar autor de livro inexistente")
    void alterAuthor_ThrowsNotFound_BookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> bookService.alterAuthor(2L, 99L));

        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve propagar exceção quando autor não existir ao alterar autor do livro")
    void alterAuthor_ThrowsNotFound_AuthorNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorService.findAuthorEntity(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado"));

        assertThrows(ResponseStatusException.class,
                () -> bookService.alterAuthor(99L, 1L));

        verify(bookRepository, never()).save(any());
    }

    // --- TESTES PARA updateAverageRating ---

    @Test
    @DisplayName("Deve atualizar o rating com o valor fornecido")
    void updateAverageRating_WithValidValue() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.updateAverageRating(1L, 4.5);

        assertEquals(4.5, book.getAverageRating());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Deve atualizar o rating para 0.0 quando o valor fornecido for null")
    void updateAverageRating_WithNullValue() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.updateAverageRating(1L, null);

        assertEquals(0.0, book.getAverageRating());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException 404 ao atualizar rating de livro inexistente")
    void updateAverageRating_ThrowsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> bookService.updateAverageRating(99L, 4.0));

        verify(bookRepository, never()).save(any());
    }
}
