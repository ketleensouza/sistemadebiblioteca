package br.edu.biblioteca.service;

import br.edu.biblioteca.structures.Vetor;
import br.edu.biblioteca.structures.ArvoreBST;
import br.edu.biblioteca.model.Usuario;

/**
 * Serviço para gerenciar usuários da biblioteca.
 * Responsável por cadastro, bloqueio/desbloqueio e listagem de usuários.
 */
public class UsuarioService {
    private ArvoreBST<Long, Usuario> usuariosPorId;
    private ArvoreBST<String, Usuario> usuariosPorEmail;
    private Vetor<Long> usuariosBloqueados;

    public UsuarioService() {
        this.usuariosPorId = new ArvoreBST<>();
        this.usuariosPorEmail = new ArvoreBST<>();
        this.usuariosBloqueados = new Vetor<>();
    }

    /**
     * Cadastra um novo usuário
     *
     * @param usuario Usuário a cadastrar
     * @return true se cadastrado com sucesso
     */
    public boolean cadastrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }

        if (usuario.getId() != null && usuariosPorId.containsKey(usuario.getId())) {
            return false; // Usuário já existe
        }

        if (usuario.getEmail() != null && usuariosPorEmail.containsKey(usuario.getEmail())) {
            return false; // Email já registrado
        }

        // Gera ID simples se não tiver
        if (usuario.getId() == null) {
            Long novoId = System.currentTimeMillis();
            usuariosPorId.put(novoId, usuario);
        } else {
            usuariosPorId.put(usuario.getId(), usuario);
        }

        if (usuario.getEmail() != null) {
            usuariosPorEmail.put(usuario.getEmail(), usuario);
        }

        return true;
    }

    /**
     * Bloqueia um usuário
     *
     * @param usuarioId ID do usuário
     * @return true se bloqueado com sucesso
     */
    public boolean bloquearUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return false;
        }

        Usuario usuario = usuariosPorId.get(usuarioId);
        if (usuario != null && !usuariosBloqueados.contains(usuarioId)) {
            usuariosBloqueados.add(usuarioId);
            return true;
        }

        return false;
    }

    /**
     * Desbloqueia um usuário
     *
     * @param usuarioId ID do usuário
     * @return true se desbloqueado com sucesso
     */
    public boolean desbloquearUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return false;
        }

        return usuariosBloqueados.remove(usuarioId);
    }

    /**
     * Verifica se um usuário está bloqueado
     *
     * @param usuarioId ID do usuário
     * @return true se bloqueado
     */
    public boolean estaUsuarioBloqueado(Long usuarioId) {
        return usuarioId != null && usuariosBloqueados.contains(usuarioId);
    }

    /**
     * Busca um usuário por ID
     *
     * @param usuarioId ID a buscar
     * @return Usuário encontrado ou null
     */
    public Usuario buscarPorId(Long usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        return usuariosPorId.get(usuarioId);
    }

    /**
     * Busca um usuário por email
     *
     * @param email Email a buscar
     * @return Usuário encontrado ou null
     */
    public Usuario buscarPorEmail(String email) {
        if (email == null) {
            return null;
        }
        return usuariosPorEmail.get(email);
    }

    /**
     * Busca usuários por nome (busca parcial)
     *
     * @param nome Nome ou parte do nome
     * @return Vetor com usuários encontrados
     */
    public Vetor<Usuario> buscarPorNome(String nome) {
        Vetor<Usuario> resultado = new Vetor<>();

        if (nome == null || nome.isEmpty()) {
            return resultado;
        }

        String nomeNorm = nome.toLowerCase();
        java.util.Iterator<Long> iter = usuariosPorId.inOrder();

        while (iter.hasNext()) {
            Usuario usuario = usuariosPorId.get(iter.next());
            if (usuario.getNome() != null && 
                usuario.getNome().toLowerCase().contains(nomeNorm)) {
                resultado.add(usuario);
            }
        }

        return resultado;
    }

    /**
     * Busca usuários por tipo
     *
     * @param tipo Tipo de usuário
     * @return Vetor com usuários do tipo especificado
     */
    public Vetor<Usuario> buscarPorTipo(Usuario.TipoUsuario tipo) {
        Vetor<Usuario> resultado = new Vetor<>();

        if (tipo == null) {
            return resultado;
        }

        java.util.Iterator<Long> iter = usuariosPorId.inOrder();

        while (iter.hasNext()) {
            Usuario usuario = usuariosPorId.get(iter.next());
            if (usuario.getTipo() == tipo) {
                resultado.add(usuario);
            }
        }

        return resultado;
    }

    /**
     * Lista todos os usuários
     *
     * @return Vetor com todos os usuários
     */
    public Vetor<Usuario> listarTodos() {
        Vetor<Usuario> resultado = new Vetor<>();
        java.util.Iterator<Long> iter = usuariosPorId.inOrder();

        while (iter.hasNext()) {
            resultado.add(usuariosPorId.get(iter.next()));
        }

        return resultado;
    }

    /**
     * Lista todos os usuários bloqueados
     *
     * @return Vetor com IDs de usuários bloqueados
     */
    public Vetor<Usuario> listarBloqueados() {
        Vetor<Usuario> resultado = new Vetor<>();

        for (Long usuarioId : usuariosBloqueados) {
            Usuario usuario = usuariosPorId.get(usuarioId);
            if (usuario != null) {
                resultado.add(usuario);
            }
        }

        return resultado;
    }

    /**
     * Atualiza um usuário
     *
     * @param usuario Usuário com dados atualizados
     * @return true se atualizado com sucesso
     */
    public boolean atualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            return false;
        }

        if (!usuariosPorId.containsKey(usuario.getId())) {
            return false;
        }

        usuariosPorId.put(usuario.getId(), usuario);

        // Atualizar email se mudou
        if (usuario.getEmail() != null) {
            usuariosPorEmail.put(usuario.getEmail(), usuario);
        }

        return true;
    }

    /**
     * Remove um usuário
     *
     * @param usuarioId ID do usuário a remover
     * @return true se removido com sucesso
     */
    public boolean removerUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return false;
        }

        Usuario removido = usuariosPorId.remove(usuarioId);
        if (removido != null) {
            if (removido.getEmail() != null) {
                usuariosPorEmail.remove(removido.getEmail());
            }
            usuariosBloqueados.remove(usuarioId);
            return true;
        }

        return false;
    }

    /**
     * Retorna o número total de usuários
     *
     * @return Número de usuários
     */
    public int getTotalUsuarios() {
        return usuariosPorId.size();
    }

    /**
     * Retorna o número de usuários bloqueados
     *
     * @return Número de bloqueados
     */
    public int getTotalBloqueados() {
        return usuariosBloqueados.size();
    }

    /**
     * Retorna o número de usuários ativos
     *
     * @return Número de ativos
     */
    public int getTotalAtivos() {
        return getTotalUsuarios() - getTotalBloqueados();
    }

    /**
     * Verifica se existe usuário com o email
     *
     * @param email Email a verificar
     * @return true se existe
     */
    public boolean existeEmail(String email) {
        return email != null && usuariosPorEmail.containsKey(email);
    }

    /**
     * Verifica se existe usuário com o ID
     *
     * @param usuarioId ID a verificar
     * @return true se existe
     */
    public boolean existeId(Long usuarioId) {
        return usuarioId != null && usuariosPorId.containsKey(usuarioId);
    }

    /**
     * Limpa todos os usuários
     */
    public void limpar() {
        usuariosPorId = new ArvoreBST<>();
        usuariosPorEmail = new ArvoreBST<>();
        usuariosBloqueados.clear();
    }
}
