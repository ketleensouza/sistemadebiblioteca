package br.edu.biblioteca.action;

import br.edu.biblioteca.model.Emprestimo;
import java.time.LocalDate;

public class AcaoDevolver implements Acao {
    private Long emprestimoId;
    private Emprestimo emprestimo;

    public AcaoDevolver(Long emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    @Override
    public void executar() {
        emprestimo.setDataDevolucao(LocalDate.now());
    }

    @Override
    public void desfazer() {
        emprestimo.setDataDevolucao(null);
    }

    @Override
    public String descricao() {
        return "Devolução do empréstimo " + emprestimoId;
    }
}
