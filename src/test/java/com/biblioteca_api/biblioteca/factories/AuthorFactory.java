package com.biblioteca_api.biblioteca.factories;

import com.biblioteca_api.biblioteca.entities.Author;

public class AuthorFactory {
    public static Author createValidAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("J.R.R. Tolkien");
        return author;
    }
}
