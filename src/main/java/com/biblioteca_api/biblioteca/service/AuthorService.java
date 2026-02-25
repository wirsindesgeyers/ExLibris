package com.biblioteca_api.biblioteca.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.AuthorRequestDTO;
import com.biblioteca_api.biblioteca.dto.AuthorResponseDTO;
import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.repository.AuthorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author findAuthorEntity(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado"));
    }

    // PEGA O AUTOR PELO ID
    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = findAuthorEntity(id);
        return AuthorResponseDTO.fromEntity(author);
    }

    // BUSCA TODOS OS AUTORES
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorResponseDTO::fromEntity)
                .toList();
    }

    // DELETE AUTOR PELO ID
    @Transactional
    public void deleteAuthorById(Long id) {
        Author author = findAuthorEntity(id);
        authorRepository.delete(author);
    }

    // CRIA UM AUTOR
    @Transactional
    public AuthorResponseDTO createAuthor(AuthorRequestDTO dto) {
        Author author = new Author();
        author.setName(dto.name());
        author.setBirthdate(dto.birthDate());

        Author savedAuthor = authorRepository.save(author);
        return AuthorResponseDTO.fromEntity(savedAuthor);
    }

    // EDITA UM AUTOR COMPLETAMENTE
    @Transactional
    public AuthorResponseDTO editAuthor(AuthorRequestDTO data, Long id) {
        Author author = findAuthorEntity(id);

        author.setBirthdate(data.birthDate());
        author.setName(data.name());

        Author updatedAuthor = authorRepository.save(author);
        return AuthorResponseDTO.fromEntity(updatedAuthor);
    }
}
