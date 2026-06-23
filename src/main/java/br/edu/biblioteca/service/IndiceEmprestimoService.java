package br.edu.biblioteca.service;

import br.edu.biblioteca.structures.Pilha;
import br.edu.biblioteca.model.Emprestimo;

/**
 * Serviço para gerenciar histórico de empréstimos com suporte a Undo/Redo.
 * Utiliza Pilha para rastrear operações e permitir reversão.
 */
public class IndiceEmprestimoService {
    private Pilha<Emprestimo> historico;
    private Pilha<Emprestimo> desfazeres;

    public IndiceEmprestimoService() {
        this.historico = new Pilha<>();
        this.desfazeres = new Pilha<>();
    }

    /**
     * Registra um empréstimo no histórico
     *
     * @param emprestimo Empréstimo a registrar
     */
    public void registrarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null) {
            historico.push(new Emprestimo(
                    emprestimo.getUsuarioId(),
                    emprestimo.getExemplarId(),
                    emprestimo.getDataEmprestimo(),
                    emprestimo.getDataPrevista()
            ));
            // Limpa desfazeres quando nova operação é feita
            desfazeres.clear();
        }
    }

    /**
     * Desfaz a última operação
     *
     * @return Empréstimo desfeito ou null
     */
    public Emprestimo desfazer() {
        if (!historico.isEmpty()) {
            Emprestimo emprestimo = historico.pop();
            desfazeres.push(emprestimo);
            return emprestimo;
        }
        return null;
    }

    /**
     * Refaz a última operação desfeita
     *
     * @return Empréstimo refeito ou null
     */
    public Emprestimo refazer() {
        if (!desfazeres.isEmpty()) {
            Emprestimo emprestimo = desfazeres.pop();
            historico.push(emprestimo);
            return emprestimo;
        }
        return null;
    }

    /**
     * Retorna o número de operações no histórico
     *
     * @return Tamanho do histórico
     */
    public int getTamanhoHistorico() {
        return historico.size();
    }

    /**
     * Verifica se há operações para desfazer
     *
     * @return true se há
     */
    public boolean temDesfazer() {
        return !historico.isEmpty();
    }

    /**
     * Verifica se há operações para refazer
     *
     * @return true se há
     */
    public boolean temRefazer() {
        return !desfazeres.isEmpty();
    }

    /**
     * Limpa todo o histórico
     */
    public void limparHistorico() {
        historico.clear();
        desfazeres.clear();
    }
}
