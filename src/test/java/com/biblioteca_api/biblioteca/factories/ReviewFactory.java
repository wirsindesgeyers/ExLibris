package com.biblioteca_api.biblioteca.factories;

import com.biblioteca_api.biblioteca.entities.Book;
import com.biblioteca_api.biblioteca.entities.Review;
import com.biblioteca_api.biblioteca.entities.User;
import java.time.LocalDate;

public class ReviewFactory {
    public static Review createValidReview(User user, Book book) {
        Review review = new Review();
        review.setId(1L);
        review.setRating((short) 2);
        review.setTitle("Titulo");
        review.setDescription("Descrição detalhada aqui");
        review.setUser(user);
        review.setBook(book);
        review.setCreatedAt(LocalDate.of(2000, 10, 13));
        return review;
    }
}
