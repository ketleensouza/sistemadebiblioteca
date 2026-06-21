package br.edu.biblioteca.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Classe que representa uma Categoria de livro no sistema de biblioteca.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Construtor padrão.
     */
    public Categoria() {
    }

    /**
     * Construtor com parâmetros.
     *
     * @param nome Nome da categoria
     */
    public Categoria(String nome) {
        this.nome = nome;
    }

    /**
     * Construtor com todos os parâmetros.
     *
     * @param id   ID da categoria
     * @param nome Nome da categoria
     */
    public Categoria(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Métodos auxiliares

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria that = (Categoria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
