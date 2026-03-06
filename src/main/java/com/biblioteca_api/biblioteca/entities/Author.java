package com.biblioteca_api.biblioteca.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O autor deve possuir um nome.")
    @Size(max = 100)
    private String name;

    @Column(name = "birth_date")
    @NotNull
    @PastOrPresent
    private LocalDate birthdate;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        // Se não for a mesma classe ou for um proxy do Hibernate, retorna false
        if (!(o instanceof Author))
            return false;
        Author other = (Author) o;
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
