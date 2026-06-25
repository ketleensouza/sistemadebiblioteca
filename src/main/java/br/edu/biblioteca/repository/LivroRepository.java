package br.edu.biblioteca.repository;

import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.model.Autor;
import br.edu.biblioteca.model.Categoria;
import java.io.IOException;
import java.util.*;

/**
 * Repository para persistência de Livros em CSV.
 */
public class LivroRepository {

    private static final String ARQUIVO = "livros";
    private static final String CABECALHO = "isbn,titulo,ano,palavrasChave";

    /**
     * Salva um livro no repositório.
     */
    public void salvar(Livro livro) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        if (linhas.isEmpty()) {
            linhas.add(CABECALHO);
        }

        String linha = FileStorage.converterParaCSV(
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getAno() != null ? livro.getAno() : "",
                livro.getPalavrasChave() != null ? livro.getPalavrasChave() : ""
        );

        linhas.add(linha);
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Carrega um livro pelo ISBN.
     */
    public Optional<Livro> buscarPorIsbn(String isbn) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (valores.get(0).equals(isbn)) {
                return Optional.of(construirLivro(valores));
            }
        }
        return Optional.empty();
    }

    /**
     * Carrega todos os livros.
     */
    public List<Livro> buscarTodos() throws IOException {
        List<Livro> livros = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            livros.add(construirLivro(FileStorage.dividirCSV(linha)));
        }
        return livros;
    }

    /**
     * Atualiza um livro.
     */
    public void atualizar(Livro livro) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i).equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linhas.get(i));
            if (valores.get(0).equals(livro.getIsbn())) {
                String novaLinha = FileStorage.converterParaCSV(
                        livro.getIsbn(),
                        livro.getTitulo(),
                        livro.getAno() != null ? livro.getAno() : "",
                        livro.getPalavrasChave() != null ? livro.getPalavrasChave() : ""
                );
                linhas.set(i, novaLinha);
                break;
            }
        }
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Remove um livro.
     */
    public void remover(String isbn) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        linhas.removeIf(linha -> {
            if (linha.equals(CABECALHO)) return false;
            List<String> valores = FileStorage.dividirCSV(linha);
            return valores.get(0).equals(isbn);
        });
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    private Livro construirLivro(List<String> valores) {
        Livro livro = new Livro();
        livro.setIsbn(valores.get(0));
        livro.setTitulo(valores.get(1));
        if (!valores.isEmpty() && valores.size() > 2 && !valores.get(2).isEmpty()) {
            try {
                livro.setAno(Integer.parseInt(valores.get(2)));
            } catch (NumberFormatException e) {
                // Ignora se não conseguir fazer parse
            }
        }
        if (!valores.isEmpty() && valores.size() > 3 && !valores.get(3).isEmpty()) {
            livro.setPalavrasChave(valores.get(3));
        }
        return livro;
    }
}
