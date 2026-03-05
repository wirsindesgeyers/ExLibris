package com.biblioteca_api.biblioteca.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.AuthorRequestDTO;
import com.biblioteca_api.biblioteca.dto.AuthorResponseDTO;
import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.factories.AuthorFactory;
import com.biblioteca_api.biblioteca.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        author = AuthorFactory.createValidAuthor();
        requestDTO = new AuthorRequestDTO("George R.R. Martin", LocalDate.of(1948, 9, 20));
    }

    @Test
    @DisplayName("Deve retornar a entidade Author quando o ID existir")
    void findAuthorEntity_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorService.findAuthorEntity(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("J.R.R. Tolkien", result.getName());
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException (404) quando o ID não existir")
    void findAuthorEntity_NotFound() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authorService.findAuthorEntity(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Autor não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar AuthorResponseDTO quando buscar autor pelo ID")
    void getAuthorById_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorResponseDTO result = authorService.getAuthorById(1L);

        assertNotNull(result);
        assertEquals("J.R.R. Tolkien", result.name());
    }

    @Test
    @DisplayName("Deve retornar lista de AuthorResponseDTO")
    void getAllAuthors_Success() {
        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<AuthorResponseDTO> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("J.R.R. Tolkien", result.get(0).name());
    }

    @Test
    @DisplayName("Deve deletar autor com sucesso")
    void deleteAuthorById_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        authorService.deleteAuthorById(1L);

        verify(authorRepository, times(1)).delete(author);
    }

    @Test
    @DisplayName("Deve criar um novo autor e retornar o DTO")
    void createAuthor_Success() {
        Author savedAuthor = new Author();
        savedAuthor.setId(2L);
        savedAuthor.setName(requestDTO.name());
        savedAuthor.setBirthdate(requestDTO.birthDate());

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        AuthorResponseDTO result = authorService.createAuthor(requestDTO);

        assertNotNull(result);
        assertEquals("George R.R. Martin", result.name());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    @DisplayName("Deve editar um autor existente e retornar o DTO atualizado")
    void editAuthor_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author updatedAuthor = new Author();
        updatedAuthor.setId(1L);
        updatedAuthor.setName(requestDTO.name());
        updatedAuthor.setBirthdate(requestDTO.birthDate());

        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        AuthorResponseDTO result = authorService.editAuthor(requestDTO, 1L);

        assertNotNull(result);
        assertEquals("George R.R. Martin", result.name());
        verify(authorRepository, times(1)).save(author);

    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver autores no banco")
    void getAllAuthors_EmptyList() {
        when(authorRepository.findAll()).thenReturn(List.of());

        List<AuthorResponseDTO> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar autor inexistente")
    void deleteAuthorById_NotFound() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            authorService.deleteAuthorById(99L);
        });

        verify(authorRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar editar autor inexistente")
    void editAuthor_NotFound() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            authorService.editAuthor(requestDTO, 99L);
        });

        verify(authorRepository, never()).save(any());
    }
}
