package com.biblioteca_api.biblioteca.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Max(5)
    @NotNull
    private short rating;

    @NotBlank(message = "title cannot be blank")
    @Size(max = 35)
    private String title;

    @Size(max = 300, message = "maximum length exceeded")
    @NotBlank(message = "description cannot be blank")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void OnCreate() {
        this.createdAt = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        // Se não for a mesma classe ou for um proxy do Hibernate, retorna false
        if (!(o instanceof Review))
            return false;
        Review other = (Review) o;
        // Se ambos os IDs forem null (não salvos), eles não são iguais (são instâncias
        // diferentes na RAM).
        // Se tiverem ID, comparamos o ID.
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        // Retorna um valor constante (ex: o nome da classe) para que o hash nunca mude
        // entre o estado transitório (id=null) e o estado gerenciado (id=1).
        return getClass().hashCode();
    }
}
