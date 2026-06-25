package br.edu.biblioteca.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Classe que representa um Exemplar (cópia física) de um Livro no sistema de biblioteca.
 */
@Entity
@Table(name = "exemplares")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn_livro", nullable = false, length = 20)
    private String isbnLivro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusExemplar status;

    /**
     * Enum que define os possíveis status de um exemplar.
     */
    public enum StatusExemplar {
        DISPONIVEL,
        EMPRESTADO,
        RESERVADO,
        INATIVO
    }

    /**
     * Construtor padrão.
     */
    public Exemplar() {
        this.status = StatusExemplar.DISPONIVEL;
    }

    /**
     * Construtor com parâmetros.
     *
     * @param isbnLivro ISBN do livro
     * @param status    Status do exemplar
     */
    public Exemplar(String isbnLivro, StatusExemplar status) {
        this.isbnLivro = isbnLivro;
        this.status = status;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbnLivro() {
        return isbnLivro;
    }

    public void setIsbnLivro(String isbnLivro) {
        this.isbnLivro = isbnLivro;
    }

    public StatusExemplar getStatus() {
        return status;
    }

    public void setStatus(StatusExemplar status) {
        this.status = status;
    }

    // Métodos auxiliares

    public boolean estaDisponivel() {
        return this.status == StatusExemplar.DISPONIVEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exemplar exemplar = (Exemplar) o;
        return Objects.equals(id, exemplar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Exemplar{" +
                "id=" + id +
                ", isbnLivro='" + isbnLivro + '\'' +
                ", status=" + status +
                '}';
    }
}
