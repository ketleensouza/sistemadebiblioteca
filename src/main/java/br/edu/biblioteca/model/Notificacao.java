package br.edu.biblioteca.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe que representa uma Notificação no sistema de biblioteca.
 */
@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private Boolean lida;

    /**
     * Construtor padrão.
     */
    public Notificacao() {
        this.lida = false;
        this.data = LocalDateTime.now();
    }

    /**
     * Construtor com parâmetros.
     *
     * @param usuarioId ID do usuário
     * @param mensagem  Mensagem da notificação
     */
    public Notificacao(Long usuarioId, String mensagem) {
        this.usuarioId = usuarioId;
        this.mensagem = mensagem;
        this.data = LocalDateTime.now();
        this.lida = false;
    }

    /**
     * Construtor com todos os parâmetros.
     *
     * @param id       ID da notificação
     * @param usuarioId ID do usuário
     * @param mensagem  Mensagem da notificação
     * @param data      Data da notificação
     * @param lida      Se a notificação foi lida
     */
    public Notificacao(Long id, Long usuarioId, String mensagem, LocalDateTime data, Boolean lida) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.mensagem = mensagem;
        this.data = data;
        this.lida = lida;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    // Métodos auxiliares

    public void marcarComoLida() {
        this.lida = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notificacao that = (Notificacao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", mensagem='" + mensagem + '\'' +
                ", data=" + data +
                ", lida=" + lida +
                '}';
    }
}
