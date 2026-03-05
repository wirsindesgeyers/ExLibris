package com.biblioteca_api.biblioteca.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.ReviewRequestDTO;
import com.biblioteca_api.biblioteca.dto.ReviewResponseDTO;
import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.entities.Book;
import com.biblioteca_api.biblioteca.entities.Review;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.factories.AuthorFactory;
import com.biblioteca_api.biblioteca.factories.BookFactory;
import com.biblioteca_api.biblioteca.factories.ReviewFactory;
import com.biblioteca_api.biblioteca.factories.UserFactory;
import com.biblioteca_api.biblioteca.repository.ReviewRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    BookService bookService;

    @Mock
    UserService userService;

    @InjectMocks
    ReviewService reviewService;

    Review review;
    ReviewResponseDTO response;
    User user;
    Book book;
    Author author;
    ReviewRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        user = UserFactory.createValidUser();
        author = AuthorFactory.createValidAuthor();
        book = BookFactory.createValidBook(author);
        review = ReviewFactory.createValidReview(user, book);
        requestDTO = new ReviewRequestDTO((short) 5, "Titulo", "descricao");
    }

    // Testes pra getReviewById

    @DisplayName("Deve retornar um dto de review  ")
    @Test
    void getReviewByIdSuccess() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        response = reviewService.getReviewById(review.getId());

        assertNotNull(response);
        assertEquals(review.getId(), response.reviewId());
        assertEquals(review.getTitle(), response.title());
        assertEquals(review.getDescription(), response.description());
        assertEquals(review.getCreatedAt(), response.createdAt());
        assertEquals(review.getRating(), response.rating());
        assertEquals(user.getName(), response.userName());
        assertEquals(book.getTitle(), response.bookTitle());

        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @DisplayName("Deve lançar uma exceção se não encontrar pelo id")
    @Test
    void getReviewByIdFail() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.getReviewById(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Review não encontrada"));
        verify(reviewRepository, times(1)).findById(99L);
    }

    // Testes para createReview

    @DisplayName("Deve criar uma review e retornar seu dto com sucesso")
    @Test
    void createReviewSuccess() {

        when(reviewRepository.existsByUserIdAndBookId(user.getId(), book.getId())).thenReturn(false);
        when(bookService.findBookEntity(book.getId())).thenReturn(book);
        when(userService.findUserEntity(user.getId())).thenReturn(user);

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        when(reviewRepository.getAverageRatingByBookId(book.getId())).thenReturn(4.8);

        ReviewResponseDTO response = reviewService.createReview(requestDTO, book.getId(), user.getId());

        assertNotNull(response);
        assertEquals(review.getId(), response.reviewId());
        assertEquals(review.getTitle(), response.title());
        assertEquals(review.getDescription(), response.description());
        assertEquals(review.getCreatedAt(), response.createdAt());
        assertEquals(review.getRating(), response.rating());
        assertEquals(user.getName(), response.userName());
        assertEquals(book.getTitle(), response.bookTitle());

        verify(reviewRepository, times(1)).existsByUserIdAndBookId(user.getId(), book.getId());
        verify(bookService, times(1)).findBookEntity(book.getId());
        verify(userService, times(1)).findUserEntity(user.getId());
        verify(reviewRepository, times(1)).save(any(Review.class));

        verify(reviewRepository, times(1)).getAverageRatingByBookId(book.getId());
        verify(bookService, times(1)).updateAverageRating(book.getId(), 4.8);
    }

    @DisplayName("Deve lançar ResponseStatusException (409) quando tentar criar uma review pra um livro que o usuário já fez review.")
    @Test
    void createReviewConflict() {
        when(reviewRepository.existsByUserIdAndBookId(user.getId(), book.getId())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.createReview(requestDTO, 1L, 1L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Usuário já enviou uma avaliação para este livro."));

        verify(reviewRepository, times(1)).existsByUserIdAndBookId(user.getId(), book.getId());

        verify(bookService, never()).findBookEntity(anyLong());
        verify(userService, never()).findUserEntity(anyLong());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    // Testes para deleteReview
    @DisplayName("Deve deletar uma review com sucesso")
    @Test
    void deleteReviewSuccess() {

        Long reviewId = review.getId();
        Long bookId = book.getId();
        Double postDeleteAvg = 3.5;

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.getAverageRatingByBookId(bookId)).thenReturn(postDeleteAvg);

        assertDoesNotThrow(() -> reviewService.deleteReview(reviewId));

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).delete(review);
        verify(reviewRepository, times(1)).getAverageRatingByBookId(bookId);
        verify(bookService, times(1)).updateAverageRating(bookId, postDeleteAvg);
    }

    @DisplayName("Deve lançar ResponseStatusException (404) ao tentar deletar uma review inexistente")
    @Test
    void deleteReview_ThrowsNotFoundWhenReviewDoesNotExist() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.deleteReview(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Review não encontrada"));

        verify(reviewRepository, times(1)).findById(99L);
        verify(reviewRepository, never()).delete(any(Review.class));
        verify(bookService, never()).updateAverageRating(anyLong(), anyDouble());
    }

    // testes pra updateReview
    @DisplayName("Deve lançar exceção ao tentar atualizar review inexistente")
    @Test
    void updateReviewThrowsExceptionWhenReviewNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> reviewService.updateReview(99L, requestDTO, book.getId(), user.getId()));

        verify(bookService, never()).findBookEntity(anyLong());
        verify(userService, never()).findUserEntity(anyLong());
        verify(reviewRepository, never()).save(any());
    }

    @DisplayName("Deve lançar exceção ao tentar atualizar review informando um livro inexistente")
    @Test
    void updateReview_ThrowsExceptionWhenBookNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(bookService.findBookEntity(99L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,
                () -> reviewService.updateReview(review.getId(), requestDTO, 99L, user.getId()));

        verify(userService, never()).findUserEntity(anyLong());
        verify(reviewRepository, never()).save(any());
    }

    @DisplayName("Deve lançar exceção ao tentar atualizar review informando um usuário inexistente")
    @Test
    void updateReview_ThrowsExceptionWhenUserNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(bookService.findBookEntity(book.getId())).thenReturn(book);
        when(userService.findUserEntity(99L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,
                () -> reviewService.updateReview(review.getId(), requestDTO, book.getId(), 99L));

        verify(reviewRepository, never()).save(any());
    }

    // testes pra updateAverageRating
    @DisplayName("Deve atualizar a review com sucesso e recalcular a média do livro")
    @Test
    void updateReview_Success() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(bookService.findBookEntity(book.getId())).thenReturn(book);
        when(userService.findUserEntity(user.getId())).thenReturn(user);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewRepository.getAverageRatingByBookId(book.getId())).thenReturn(4.0);

        ReviewResponseDTO response = reviewService.updateReview(review.getId(), requestDTO, book.getId(), user.getId());

        assertNotNull(response);
        verify(reviewRepository, times(1)).save(review);
        verify(bookService, times(1)).updateAverageRating(book.getId(), 4.0);
    }

    // testes para listReviewsFromBook
    @DisplayName("Deve lançar exceção ao listar reviews de um livro inexistente")
    @Test
    void listReviewsFromBook_ThrowsExceptionWhenBookNotFound() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(bookService).validateBookExists(99L);

        assertThrows(ResponseStatusException.class, () -> reviewService.listReviewsFromBook(99L));

        verify(reviewRepository, never()).findByBookId(anyLong());
    }

    @DisplayName("Deve retornar lista vazia quando o livro não tiver nenhuma review")
    @Test
    void listReviewsFromBook_ReturnsEmptyList() {
        doNothing().when(bookService).validateBookExists(book.getId());
        when(reviewRepository.findByBookId(book.getId())).thenReturn(List.of());

        List<ReviewResponseDTO> result = reviewService.listReviewsFromBook(book.getId());

        assertTrue(result.isEmpty());
        verify(reviewRepository, times(1)).findByBookId(book.getId());
    }

    @DisplayName("Deve retornar lista populada de reviews do livro com sucesso")
    @Test
    void listReviewsFromBook_ReturnsPopulatedList() {
        doNothing().when(bookService).validateBookExists(book.getId());
        when(reviewRepository.findByBookId(book.getId())).thenReturn(List.of(review));

        List<ReviewResponseDTO> result = reviewService.listReviewsFromBook(book.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findByBookId(book.getId());
    }

    // testes pra listReviewsFromUser
    @DisplayName("Deve lançar exceção ao listar reviews de um usuário inexistente")
    @Test
    void listReviewsFromUser_ThrowsExceptionWhenUserNotFound() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).validateUserExists(99L);

        assertThrows(ResponseStatusException.class, () -> reviewService.listReviewsFromUser(99L));

        verify(reviewRepository, never()).findByUserId(anyLong());
    }

    @DisplayName("Deve retornar lista vazia quando o usuário não tiver feito nenhuma review")
    @Test
    void listReviewsFromUser_ReturnsEmptyList() {
        doNothing().when(userService).validateUserExists(user.getId());
        when(reviewRepository.findByUserId(user.getId())).thenReturn(List.of());

        List<ReviewResponseDTO> result = reviewService.listReviewsFromUser(user.getId());

        assertTrue(result.isEmpty());
        verify(reviewRepository, times(1)).findByUserId(user.getId());
    }

    @DisplayName("Deve retornar lista populada de reviews do usuário com sucesso")
    @Test
    void listReviewsFromUser_ReturnsPopulatedList() {
        doNothing().when(userService).validateUserExists(user.getId());
        when(reviewRepository.findByUserId(user.getId())).thenReturn(List.of(review));

        List<ReviewResponseDTO> result = reviewService.listReviewsFromUser(user.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findByUserId(user.getId());
    }

}
