package br.edu.biblioteca.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Classe que representa um Autor no sistema de biblioteca.
 */
@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    /**
     * Construtor padrão.
     */
    public Autor() {
    }

    /**
     * Construtor com parâmetros.
     *
     * @param nome Nome do autor
     */
    public Autor(String nome) {
        this.nome = nome;
    }

    /**
     * Construtor com todos os parâmetros.
     *
     * @param id   ID do autor
     * @param nome Nome do autor
     */
    public Autor(Long id, String nome) {
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
        Autor autor = (Autor) o;
        return Objects.equals(id, autor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
