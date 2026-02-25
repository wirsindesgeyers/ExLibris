package com.biblioteca_api.biblioteca.service;

import com.biblioteca_api.biblioteca.dto.BookRequestDTO;
import com.biblioteca_api.biblioteca.dto.BookResponseDTO;
import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.entities.Book;
import com.biblioteca_api.biblioteca.infra.exceptions.BookAlreadyExistsException;
import com.biblioteca_api.biblioteca.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    // RETRIEVES BOOK (ENTITY) BY ITS ID
    protected Book findBookEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    // VALIDATES IF THE BOOK EXISTS
    public void validateBookExists(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado");
        }
    }

    // RETRIEVES A RESPONSE DTO OF THE BOOK
    public BookResponseDTO getBookById(Long id) {
        Book book = findBookEntity(id);
        return BookResponseDTO.fromEntity(book);
    }

    // LISTS ALL BOOKS
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponseDTO::fromEntity)
                .toList();
    }

    // CREATES BOOKS
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO dto) {
        if (bookRepository.existsByIsbn(dto.isbn())) {
            throw new BookAlreadyExistsException("Já existe um livro cadastrado com este ISBN.");
        }

        Author author = authorService.findAuthorEntity(dto.authorId());

        Book book = new Book();
        book.setTitle(dto.title());
        book.setIsbn(dto.isbn());
        book.setPrice(dto.price());
        book.setPublishedDate(dto.publishedDate());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return BookResponseDTO.fromEntity(savedBook);
    }

    // DELETES BOOK BY ITS ID
    @Transactional
    public void deleteBookById(Long id) {
        Book book = findBookEntity(id);
        bookRepository.delete(book);
    }

    // UPDATE BOOK ENTIRELY
    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO dto) {
        Book book = findBookEntity(id);

        book.setTitle(dto.title());
        book.setIsbn(dto.isbn());
        book.setPrice(dto.price());
        book.setPublishedDate(dto.publishedDate());

        // Verifies if author has changed to not repeat unnecessary searches
        if (book.getAuthor() == null || !book.getAuthor().getId().equals(dto.authorId())) {
            Author newAuthor = authorService.findAuthorEntity(dto.authorId());
            book.setAuthor(newAuthor);
        }

        Book updatedBook = bookRepository.save(book);
        return BookResponseDTO.fromEntity(updatedBook);
    }

    // Alters a books' author
    @Transactional
    public BookResponseDTO alterAuthor(Long authorId, Long bookId) {
        Book book = findBookEntity(bookId);
        Author author = authorService.findAuthorEntity(authorId);

        book.setAuthor(author);
        Book updatedBook = bookRepository.save(book);

        return BookResponseDTO.fromEntity(updatedBook);
    }

    @Transactional
    public void updateAverageRating(Long bookId, Double newAverage) {
        Book book = findBookEntity(bookId);
        book.setAverageRating(newAverage != null ? newAverage : 0.0);
        bookRepository.save(book);
    }
}
