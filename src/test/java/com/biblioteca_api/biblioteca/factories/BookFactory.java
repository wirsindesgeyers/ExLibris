package com.biblioteca_api.biblioteca.factories;

import com.biblioteca_api.biblioteca.entities.Author;
import com.biblioteca_api.biblioteca.entities.Book;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookFactory {
    public static Book createValidBook(Author author) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Título bacana");
        book.setIsbn("9782306123315");
        book.setPrice(new BigDecimal("22.00"));
        book.setPublishedDate(LocalDate.of(2000, 10, 13));
        book.setAuthor(author);
        book.setAverageRating(2.50);
        return book;
    }
}
