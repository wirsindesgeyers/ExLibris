package com.biblioteca_api.biblioteca.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.ReviewRequestDTO;
import com.biblioteca_api.biblioteca.dto.ReviewResponseDTO; // Importando o DTO novo
import com.biblioteca_api.biblioteca.entities.Book;
import com.biblioteca_api.biblioteca.entities.Review;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final UserService userService;

    // RETrieveS THE REVIEW ENTITY
    private Review findReviewEntity(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review não encontrada"));
    }

    // RETRIEVE A REVIEW (Retorna DTO)
    public ReviewResponseDTO getReviewById(Long id) {
        Review review = findReviewEntity(id);
        return ReviewResponseDTO.fromEntity(review);
    }

    // CREATE A REVIEW
    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO dto, Long bookId, Long userId) {
        if (reviewRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Usuário já enviou uma avaliação para este livro.");
        }

        Book book = bookService.findBookEntity(bookId);
        User user = userService.findUserEntity(userId);

        Review review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setDescription(dto.description());
        review.setTitle(dto.title());
        review.setRating(dto.rating());

        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDTO.fromEntity(savedReview);
    }

    // DELETE A REVIEW
    @Transactional
    public void deleteReview(Long id) {
        Review review = findReviewEntity(id);
        reviewRepository.delete(review);
    }

    // UPDATE A REVIEW
    @Transactional
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto, Long bookId, Long userId) {
        Review review = findReviewEntity(id);
        Book book = bookService.findBookEntity(bookId);
        User user = userService.findUserEntity(userId);

        review.setBook(book);
        review.setUser(user);
        review.setDescription(dto.description());
        review.setTitle(dto.title());
        review.setRating(dto.rating());

        Review updatedReview = reviewRepository.save(review);
        return ReviewResponseDTO.fromEntity(updatedReview);
    }

    // RETRIEVES ALL REVIEWS FROM A SPECIFIC BOOK
    public List<ReviewResponseDTO> listReviewsFromBook(Long bookId) {
        bookService.validateBookExists(bookId);

        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(ReviewResponseDTO::fromEntity)
                .toList();
    }

    // RETRIEVES ALL REVIEWS FROM A SPECIFIC User
    public List<ReviewResponseDTO> listReviewsFromUser(Long userId) {
        userService.ValidateUserExists(userId);
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(ReviewResponseDTO::fromEntity)
                .toList();
    }
}
