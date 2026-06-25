package br.edu.biblioteca.repository;

import br.edu.biblioteca.model.Usuario;
import java.io.IOException;
import java.util.*;

/**
 * Repository para persistência de Usuários em CSV.
 */
public class UsuarioRepository {

    private static final String ARQUIVO = "usuarios";
    private static final String CABECALHO = "id,nome,tipo,email";
    private static long proximoId = 1;

    static {
        carregarProximoId();
    }

    /**
     * Salva um usuário no repositório.
     */
    public void salvar(Usuario usuario) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        if (linhas.isEmpty()) {
            linhas.add(CABECALHO);
        }

        if (usuario.getId() == null) {
            usuario.setId(proximoId++);
        }

        String linha = FileStorage.converterParaCSV(
                usuario.getId(),
                usuario.getNome(),
                usuario.getTipo().name(),
                usuario.getEmail()
        );

        linhas.add(linha);
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Carrega um usuário pelo ID.
     */
    public Optional<Usuario> buscarPorId(Long id) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (Long.parseLong(valores.get(0)) == id) {
                return Optional.of(construirUsuario(valores));
            }
        }
        return Optional.empty();
    }

    /**
     * Carrega um usuário pelo email.
     */
    public Optional<Usuario> buscarPorEmail(String email) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (valores.get(3).equals(email)) {
                return Optional.of(construirUsuario(valores));
            }
        }
        return Optional.empty();
    }

    /**
     * Carrega todos os usuários.
     */
    public List<Usuario> buscarTodos() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            usuarios.add(construirUsuario(FileStorage.dividirCSV(linha)));
        }
        return usuarios;
    }

    /**
     * Atualiza um usuário.
     */
    public void atualizar(Usuario usuario) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i).equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linhas.get(i));
            if (Long.parseLong(valores.get(0)) == usuario.getId()) {
                String novaLinha = FileStorage.converterParaCSV(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getTipo().name(),
                        usuario.getEmail()
                );
                linhas.set(i, novaLinha);
                break;
            }
        }
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Remove um usuário.
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

    private Usuario construirUsuario(List<String> valores) {
        Usuario usuario = new Usuario();
        if (!valores.isEmpty() && !valores.get(0).isEmpty()) {
            try {
                usuario.setId(Long.parseLong(valores.get(0)));
            } catch (NumberFormatException e) {
                // Ignora se não conseguir fazer parse
            }
        }
        if (valores.size() > 1) {
            usuario.setNome(valores.get(1));
        }
        if (valores.size() > 2) {
            try {
                usuario.setTipo(Usuario.TipoUsuario.valueOf(valores.get(2)));
            } catch (IllegalArgumentException e) {
                usuario.setTipo(Usuario.TipoUsuario.ALUNO);
            }
        }
        if (valores.size() > 3) {
            usuario.setEmail(valores.get(3));
        }
        return usuario;
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
