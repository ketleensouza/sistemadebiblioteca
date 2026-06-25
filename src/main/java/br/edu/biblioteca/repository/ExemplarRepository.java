package br.edu.biblioteca.repository;

import br.edu.biblioteca.model.Exemplar;
import java.io.IOException;
import java.util.*;

/**
 * Repository para persistência de Exemplares em CSV.
 */
public class ExemplarRepository {

    private static final String ARQUIVO = "exemplares";
    private static final String CABECALHO = "id,isbnLivro,status";
    private static long proximoId = 1;

    static {
        carregarProximoId();
    }

    /**
     * Salva um exemplar no repositório.
     */
    public void salvar(Exemplar exemplar) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        if (linhas.isEmpty()) {
            linhas.add(CABECALHO);
        }

        if (exemplar.getId() == null) {
            exemplar.setId(proximoId++);
        }

        String linha = FileStorage.converterParaCSV(
                exemplar.getId(),
                exemplar.getIsbnLivro(),
                exemplar.getStatus().name()
        );

        linhas.add(linha);
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Carrega um exemplar pelo ID.
     */
    public Optional<Exemplar> buscarPorId(Long id) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (Long.parseLong(valores.get(0)) == id) {
                return Optional.of(construirExemplar(valores));
            }
        }
        return Optional.empty();
    }

    /**
     * Carrega exemplares por ISBN do livro.
     */
    public List<Exemplar> buscarPorIsbn(String isbn) throws IOException {
        List<Exemplar> exemplares = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (valores.get(1).equals(isbn)) {
                exemplares.add(construirExemplar(valores));
            }
        }
        return exemplares;
    }

    /**
     * Carrega exemplares por status.
     */
    public List<Exemplar> buscarPorStatus(Exemplar.StatusExemplar status) throws IOException {
        List<Exemplar> exemplares = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (valores.get(2).equals(status.name())) {
                exemplares.add(construirExemplar(valores));
            }
        }
        return exemplares;
    }

    /**
     * Carrega todos os exemplares.
     */
    public List<Exemplar> buscarTodos() throws IOException {
        List<Exemplar> exemplares = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            exemplares.add(construirExemplar(FileStorage.dividirCSV(linha)));
        }
        return exemplares;
    }

    /**
     * Atualiza um exemplar.
     */
    public void atualizar(Exemplar exemplar) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i).equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linhas.get(i));
            if (Long.parseLong(valores.get(0)) == exemplar.getId()) {
                String novaLinha = FileStorage.converterParaCSV(
                        exemplar.getId(),
                        exemplar.getIsbnLivro(),
                        exemplar.getStatus().name()
                );
                linhas.set(i, novaLinha);
                break;
            }
        }
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Remove um exemplar.
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

    private Exemplar construirExemplar(List<String> valores) {
        Exemplar exemplar = new Exemplar();
        if (!valores.isEmpty() && !valores.get(0).isEmpty()) {
            try {
                exemplar.setId(Long.parseLong(valores.get(0)));
            } catch (NumberFormatException e) {
                // Ignora se não conseguir fazer parse
            }
        }
        if (valores.size() > 1) {
            exemplar.setIsbnLivro(valores.get(1));
        }
        if (valores.size() > 2) {
            try {
                exemplar.setStatus(Exemplar.StatusExemplar.valueOf(valores.get(2)));
            } catch (IllegalArgumentException e) {
                exemplar.setStatus(Exemplar.StatusExemplar.DISPONIVEL);
            }
        }
        return exemplar;
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
