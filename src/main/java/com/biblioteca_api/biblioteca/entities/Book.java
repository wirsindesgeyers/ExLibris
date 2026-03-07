package com.biblioteca_api.biblioteca.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "isbn")
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(nullable = false)
    @Min(value = 0, message = "O valor não pode ser menor que 0.")
    private BigDecimal price;

    @Column(name = "published_date", nullable = false)
    @PastOrPresent(message = "O livro não pode ter sido publicado no futuro.")
    private LocalDate publishedDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull
    private Author author;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @Min(0)
    @Max(5)
    @Column(name = "average_rating")
    private Double averageRating = 0.0;
}
