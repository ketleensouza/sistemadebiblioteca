package br.edu.biblioteca.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa um Empréstimo no sistema de biblioteca.
 */
@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "exemplar_id", nullable = false)
    private Long exemplarId;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(length = 50)
    private String status;

    /**
     * Construtor padrão.
     */
    public Emprestimo() {
    }

    /**
     * Construtor com parâmetros principais.
     *
     * @param usuarioId      ID do usuário
     * @param exemplarId     ID do exemplar
     * @param dataEmprestimo Data do empréstimo
     * @param dataPrevista   Data prevista de devolução
     */
    public Emprestimo(Long usuarioId, Long exemplarId, LocalDate dataEmprestimo, LocalDate dataPrevista) {
        this.usuarioId = usuarioId;
        this.exemplarId = exemplarId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
        this.status = "ATIVO";
    }

    /**
     * Construtor com todos os parâmetros.
     *
     * @param id              ID do empréstimo
     * @param usuarioId       ID do usuário
     * @param exemplarId      ID do exemplar
     * @param dataEmprestimo  Data do empréstimo
     * @param dataPrevista    Data prevista de devolução
     * @param dataDevolucao   Data da devolução
     * @param status          Status do empréstimo
     */
    public Emprestimo(Long id, Long usuarioId, Long exemplarId, LocalDate dataEmprestimo,
                      LocalDate dataPrevista, LocalDate dataDevolucao, String status) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.exemplarId = exemplarId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
        this.dataDevolucao = dataDevolucao;
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

    public Long getExemplarId() {
        return exemplarId;
    }

    public void setExemplarId(Long exemplarId) {
        this.exemplarId = exemplarId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Métodos auxiliares

    public boolean estaAtrasado() {
        return dataDevolucao == null && LocalDate.now().isAfter(dataPrevista);
    }

    public long calcularDiasAtraso() {
        LocalDate dataReferencia = dataDevolucao != null ? dataDevolucao : LocalDate.now();
        if (dataReferencia.isAfter(dataPrevista)) {
            return java.time.temporal.ChronoUnit.DAYS.between(dataPrevista, dataReferencia);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprestimo that = (Emprestimo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", exemplarId=" + exemplarId +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataPrevista=" + dataPrevista +
                ", dataDevolucao=" + dataDevolucao +
                ", status='" + status + '\'' +
                '}';
    }
}
