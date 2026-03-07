package com.biblioteca_api.biblioteca.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "loan_price")
    @NotNull(message = "Valor do empréstimo não pode ser nulo")
    private BigDecimal loanPrice;

    @Column(name = "expiration_date")
    @NotNull(message = "É necessário ter uma data de fim de emprestimo.")
    @Future(message = "Data de expiração de empréstimo não pode ser no passado.")
    private LocalDate expirationDate;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        // Se não for a mesma classe ou for um proxy do Hibernate, retorna false
        if (!(o instanceof Loan))
            return false;
        Loan other = (Loan) o;
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
