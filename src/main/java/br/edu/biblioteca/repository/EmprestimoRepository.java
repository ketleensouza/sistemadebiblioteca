package br.edu.biblioteca.repository;

import br.edu.biblioteca.model.Emprestimo;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Repository para persistência de Empréstimos em CSV.
 */
public class EmprestimoRepository {

    private static final String ARQUIVO = "emprestimos";
    private static final String CABECALHO = "id,usuarioId,exemplarId,dataEmprestimo,dataPrevista,dataDevolucao,status";
    private static long proximoId = 1;

    static {
        carregarProximoId();
    }

    /**
     * Salva um empréstimo no repositório.
     */
    public void salvar(Emprestimo emprestimo) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        if (linhas.isEmpty()) {
            linhas.add(CABECALHO);
        }

        if (emprestimo.getId() == null) {
            emprestimo.setId(proximoId++);
        }

        String linha = FileStorage.converterParaCSV(
                emprestimo.getId(),
                emprestimo.getUsuarioId(),
                emprestimo.getExemplarId(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevista(),
                emprestimo.getDataDevolucao() != null ? emprestimo.getDataDevolucao() : "",
                emprestimo.getStatus() != null ? emprestimo.getStatus() : ""
        );

        linhas.add(linha);
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Carrega um empréstimo pelo ID.
     */
    public Optional<Emprestimo> buscarPorId(Long id) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (Long.parseLong(valores.get(0)) == id) {
                return Optional.of(construirEmprestimo(valores));
            }
        }
        return Optional.empty();
    }

    /**
     * Carrega empréstimos por usuário.
     */
    public List<Emprestimo> buscarPorUsuario(Long usuarioId) throws IOException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (Long.parseLong(valores.get(1)) == usuarioId) {
                emprestimos.add(construirEmprestimo(valores));
            }
        }
        return emprestimos;
    }

    /**
     * Carrega empréstimos por exemplar.
     */
    public List<Emprestimo> buscarPorExemplar(Long exemplarId) throws IOException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (Long.parseLong(valores.get(2)) == exemplarId) {
                emprestimos.add(construirEmprestimo(valores));
            }
        }
        return emprestimos;
    }

    /**
     * Carrega empréstimos por status.
     */
    public List<Emprestimo> buscarPorStatus(String status) throws IOException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (valores.get(6).equals(status)) {
                emprestimos.add(construirEmprestimo(valores));
            }
        }
        return emprestimos;
    }

    /**
     * Carrega todos os empréstimos.
     */
    public List<Emprestimo> buscarTodos() throws IOException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            emprestimos.add(construirEmprestimo(FileStorage.dividirCSV(linha)));
        }
        return emprestimos;
    }

    /**
     * Atualiza um empréstimo.
     */
    public void atualizar(Emprestimo emprestimo) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i).equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linhas.get(i));
            if (Long.parseLong(valores.get(0)) == emprestimo.getId()) {
                String novaLinha = FileStorage.converterParaCSV(
                        emprestimo.getId(),
                        emprestimo.getUsuarioId(),
                        emprestimo.getExemplarId(),
                        emprestimo.getDataEmprestimo(),
                        emprestimo.getDataPrevista(),
                        emprestimo.getDataDevolucao() != null ? emprestimo.getDataDevolucao() : "",
                        emprestimo.getStatus() != null ? emprestimo.getStatus() : ""
                );
                linhas.set(i, novaLinha);
                break;
            }
        }
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Remove um empréstimo.
     */
    public void remover(Long id) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        linhas.removeIf(linha -> {
            if (linha.equals(CABECALHO)) return false;
            List<String> valores = FileStorage.dividirCSV(linha);
            return Long.parseLong(valores.get(0)) == id;
        });
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    private Emprestimo construirEmprestimo(List<String> valores) {
        Emprestimo emprestimo = new Emprestimo();
        try {
            if (!valores.isEmpty() && !valores.get(0).isEmpty()) {
                emprestimo.setId(Long.parseLong(valores.get(0)));
            }
            if (valores.size() > 1 && !valores.get(1).isEmpty()) {
                emprestimo.setUsuarioId(Long.parseLong(valores.get(1)));
            }
            if (valores.size() > 2 && !valores.get(2).isEmpty()) {
                emprestimo.setExemplarId(Long.parseLong(valores.get(2)));
            }
            if (valores.size() > 3 && !valores.get(3).isEmpty()) {
                emprestimo.setDataEmprestimo(LocalDate.parse(valores.get(3)));
            }
            if (valores.size() > 4 && !valores.get(4).isEmpty()) {
                emprestimo.setDataPrevista(LocalDate.parse(valores.get(4)));
            }
            if (valores.size() > 5 && !valores.get(5).isEmpty()) {
                emprestimo.setDataDevolucao(LocalDate.parse(valores.get(5)));
            }
            if (valores.size() > 6 && !valores.get(6).isEmpty()) {
                emprestimo.setStatus(valores.get(6));
            }
        } catch (Exception e) {
            System.err.println("Erro ao construir empréstimo: " + e.getMessage());
        }
        return emprestimo;
    }

    private static void carregarProximoId() {
        try {
            List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
            for (String linha : linhas) {
                if (linha.equals(CABECALHO)) continue;
                List<String> valores = FileStorage.dividirCSV(linha);
                long id = Long.parseLong(valores.get(0));
                if (id >= proximoId) {
                    proximoId = id + 1;
                }
            }
        } catch (IOException e) {
            proximoId = 1;
        }
    }
}
