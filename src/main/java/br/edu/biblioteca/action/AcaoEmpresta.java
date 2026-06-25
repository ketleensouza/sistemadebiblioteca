package br.edu.biblioteca.action;

import br.edu.biblioteca.model.Emprestimo;
import java.time.LocalDate;

public class AcaoEmpresta implements Acao {
    private Long exemplarId;
    private Long usuarioId;

    public AcaoEmpresta(Long exemplarId, Long usuarioId) {
        this.exemplarId = exemplarId;
        this.usuarioId = usuarioId;
    }

    @Override
    public void executar() {
        LocalDate dataEmprestimo = LocalDate.now();
        LocalDate dataPrevista = dataEmprestimo.plusDays(14);
        new Emprestimo(usuarioId, exemplarId, dataEmprestimo, dataPrevista);
    }

    @Override
    public void desfazer() {
        // Desfazer empréstimo
    }

    @Override
    public String descricao() {
        return "Empréstimo do exemplar " + exemplarId + " para usuário " + usuarioId;
    }
}
