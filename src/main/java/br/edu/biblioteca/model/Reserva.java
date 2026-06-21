package br.edu.biblioteca.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa uma Reserva no sistema de biblioteca.
 */
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "isbn_livro", nullable = false, length = 20)
    private String isbnLivro;

    @Column(name = "data_reserva", nullable = false)
    private LocalDate dataReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status;

    /**
     * Enum que define os status de uma reserva.
     */
    public enum StatusReserva {
        RESERVADO,
        CANCELADO,
        RETIRADO
    }

    /**
     * Construtor padrão.
     */
    public Reserva() {
        this.status = StatusReserva.RESERVADO;
        this.dataReserva = LocalDate.now();
    }

    /**
     * Construtor com parâmetros.
     *
     * @param usuarioId  ID do usuário
     * @param isbnLivro  ISBN do livro
     * @param dataReserva Data da reserva
     */
    public Reserva(Long usuarioId, String isbnLivro, LocalDate dataReserva) {
        this.usuarioId = usuarioId;
        this.isbnLivro = isbnLivro;
        this.dataReserva = dataReserva;
        this.status = StatusReserva.RESERVADO;
    }

    /**
     * Construtor com todos os parâmetros.
     *
     * @param id         ID da reserva
     * @param usuarioId  ID do usuário
     * @param isbnLivro  ISBN do livro
     * @param dataReserva Data da reserva
     * @param status     Status da reserva
     */
    public Reserva(Long id, Long usuarioId, String isbnLivro, LocalDate dataReserva, StatusReserva status) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.isbnLivro = isbnLivro;
        this.dataReserva = dataReserva;
        this.status = status;
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

    public String getIsbnLivro() {
        return isbnLivro;
    }

    public void setIsbnLivro(String isbnLivro) {
        this.isbnLivro = isbnLivro;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    // Métodos auxiliares

    public boolean estaVigente() {
        return this.status == StatusReserva.RESERVADO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(id, reserva.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", isbnLivro='" + isbnLivro + '\'' +
                ", dataReserva=" + dataReserva +
                ", status=" + status +
                '}';
    }
}
