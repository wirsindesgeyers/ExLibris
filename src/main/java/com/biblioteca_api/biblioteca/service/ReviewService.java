package com.biblioteca_api.biblioteca.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.ReviewRequestDTO;
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

    // RETRIEVE A REVIEW
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review não encontrada"));
    }

    // CREATE A REVIEW
    @Transactional
    public Review createReview(ReviewRequestDTO dto, Long bookId, Long userId) {
        if (reviewRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Usuário já enviou uma avaliação para este livro.");
        }

        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userId);

        Review review = new Review();

        review.setBook(book);
        review.setUser(user);
        review.setDescription(dto.description());
        review.setTitle(dto.title());
        review.setRating(dto.rating());

        return reviewRepository.save(review);
    }

    // DELETE A REVIEW
    @Transactional
    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        reviewRepository.delete(review);
    }

    // UPDATE A REVIEW
    @Transactional
    public Review updateReview(Long id, ReviewRequestDTO dto, Long bookId, Long userId) {
        Review review = getReviewById(id);
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userId);

        review.setBook(book);
        review.setUser(user);
        review.setDescription(dto.description());
        review.setTitle(dto.title());
        review.setRating(dto.rating());

        return reviewRepository.save(review);
    }

}
