package br.edu.biblioteca.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Classe utilitária para operações de leitura e escrita em arquivos CSV/TXT.
 */
public class FileStorage {

    private static final String DATA_DIR = "data";
    private static final String ENCODING = "UTF-8";

    static {
        criarDiretorioDados();
    }

    /**
     * Cria o diretório de dados se não existir.
     */
    private static void criarDiretorioDados() {
        try {
            Path dir = Paths.get(DATA_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    /**
     * Salva linhas em um arquivo CSV.
     */
    public static void salvarCSV(String nomeArquivo, List<String> linhas) throws IOException {
        Path caminho = Paths.get(DATA_DIR, nomeArquivo + ".csv");
        Files.write(caminho, linhas, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Carrega linhas de um arquivo CSV.
     */
    public static List<String> carregarCSV(String nomeArquivo) throws IOException {
        Path caminho = Paths.get(DATA_DIR, nomeArquivo + ".csv");
        if (!Files.exists(caminho)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(caminho, StandardCharsets.UTF_8);
    }

    /**
     * Salva uma linha em um arquivo CSV (append).
     */
    public static void adicionarLinhaCSV(String nomeArquivo, String linha) throws IOException {
        Path caminho = Paths.get(DATA_DIR, nomeArquivo + ".csv");
        Files.write(caminho, (linha + "\n").getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Converte lista de valores em uma linha CSV.
     */
    public static String converterParaCSV(Object... valores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) sb.append(",");
            String valor = String.valueOf(valores[i]);
            if (valor.contains(",") || valor.contains("\"")) {
                valor = "\"" + valor.replace("\"", "\"\"") + "\"";
            }
            sb.append(valor);
        }
        return sb.toString();
    }

    /**
     * Divide uma linha CSV em componentes.
     */
    public static List<String> dividirCSV(String linha) {
        List<String> valores = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean dentroAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"') {
                if (i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                } else {
                    dentroAspas = !dentroAspas;
                }
            } else if (c == ',' && !dentroAspas) {
                valores.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        valores.add(sb.toString());
        return valores;
    }

    /**
     * Retorna o caminho do diretório de dados.
     */
    public static String getDiretorioDados() {
        return DATA_DIR;
    }
}
