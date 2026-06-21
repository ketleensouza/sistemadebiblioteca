package br.edu.biblioteca.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Classe que representa um Livro no sistema de biblioteca.
 */
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @Column(length = 20)
    private String isbn;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(name = "ano_publicacao")
    private Integer ano;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "livro_categoria",
            joinColumns = @JoinColumn(name = "isbn_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private Set<Categoria> categorias = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "isbn_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_autor")
    )
    private Set<Autor> autores = new HashSet<>();

    @Column(name = "palavras_chave", columnDefinition = "TEXT")
    private String palavrasChave;

    /**
     * Construtor padrão.
     */
    public Livro() {
    }

    /**
     * Construtor com parâmetros principais.
     *
     * @param isbn   ISBN do livro
     * @param titulo Título do livro
     * @param ano    Ano de publicação
     */
    public Livro(String isbn, String titulo, Integer ano) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.ano = ano;
    }

    /**
     * Construtor com todos os parâmetros.
     *
     * @param isbn           ISBN do livro
     * @param titulo         Título do livro
     * @param ano            Ano de publicação
     * @param categorias     Conjunto de categorias
     * @param autores        Conjunto de autores
     * @param palavrasChave  Palavras-chave para busca
     */
    public Livro(String isbn, String titulo, Integer ano, Set<Categoria> categorias,
                 Set<Autor> autores, String palavrasChave) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.ano = ano;
        this.categorias = categorias;
        this.autores = autores;
        this.palavrasChave = palavrasChave;
    }

    // Getters e Setters

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    public String getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    // Métodos auxiliares

    public void adicionarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
    }

    public void adicionarAutor(Autor autor) {
        this.autores.add(autor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(isbn, livro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Livro{" +
                "isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", ano=" + ano +
                ", palavrasChave='" + palavrasChave + '\'' +
                '}';
    }
}
