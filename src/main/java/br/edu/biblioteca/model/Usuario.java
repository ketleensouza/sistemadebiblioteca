package br.edu.biblioteca.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Classe que representa um Usuário do sistema de biblioteca.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /**
     * Enum que define os tipos de usuário.
     */
    public enum TipoUsuario {
        ALUNO,
        PROFESSOR,
        SERVIDOR
    }

    /**
     * Construtor padrão.
     */
    public Usuario() {
    }

    /**
     * Construtor com parâmetros.
     *
     * @param nome  Nome do usuário
     * @param tipo  Tipo de usuário
     * @param email Email do usuário
     */
    public Usuario(String nome, TipoUsuario tipo, String email) {
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Métodos auxiliares

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo=" + tipo +
                ", email='" + email + '\'' +
                '}';
    }
}
