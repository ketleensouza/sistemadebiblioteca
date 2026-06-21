package br.edu.biblioteca.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Classe que representa uma Multa no sistema de biblioteca.
 */
@Entity
@Table(name = "multas")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emprestimo_id", nullable = false)
    private Long emprestimoId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "dias_atraso", nullable = false)
    private Integer diasAtraso;

    @Column(nullable = false)
    private Boolean quitada;

    /**
     * Construtor padrão.
     */
    public Multa() {
        this.quitada = false;
    }

    /**
     * Construtor com parâmetros.
     *
     * @param emprestimoId ID do empréstimo
     * @param valor       Valor da multa
     * @param diasAtraso  Número de dias em atraso
     */
    public Multa(Long emprestimoId, BigDecimal valor, Integer diasAtraso) {
        this.emprestimoId = emprestimoId;
        this.valor = valor;
        this.diasAtraso = diasAtraso;
        this.quitada = false;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public Long getEmprestimoId() {
        return emprestimoId;
    }

    public void setEmprestimoId(Long emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(Integer diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public Boolean getQuitada() {
        return quitada;
    }

    public void setQuitada(Boolean quitada) {
        this.quitada = quitada;
    }

    // Métodos auxiliares

    public void quitarMulta() {
        this.quitada = true;
    }

    public boolean estaPendente() {
        return !quitada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Multa multa = (Multa) o;
        return Objects.equals(id, multa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Multa{" +
                "id=" + id +
                ", emprestimoId=" + emprestimoId +
                ", valor=" + valor +
                ", diasAtraso=" + diasAtraso +
                ", quitada=" + quitada +
                '}';
    }
}
