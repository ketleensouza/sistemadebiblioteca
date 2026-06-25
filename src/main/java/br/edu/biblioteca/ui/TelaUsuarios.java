package br.edu.biblioteca.ui;

import br.edu.biblioteca.repository.UsuarioRepository;
import java.util.Scanner;

/**
 * Tela para gerenciar usuários.
 */
public class TelaUsuarios {

    private Scanner scanner = new Scanner(System.in);
    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public void exibir() {
        boolean ativo = true;
        while (ativo) {
            exibirMenu();
            int opcao = lerOpcao();
            ativo = executarOpcao(opcao);
        }
    }

    private void exibirMenu() {
        limparTela();
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║       GERENCIAR USUÁRIOS              ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1 - Listar Usuários                  ║");
        System.out.println("║ 2 - Buscar por ID                    ║");
        System.out.println("║ 3 - Buscar por Email                 ║");
        System.out.println("║ 4 - Cadastrar Novo Usuário           ║");
        System.out.println("║ 5 - Atualizar Usuário                ║");
        System.out.println("║ 6 - Remover Usuário                  ║");
        System.out.println("║ 0 - Voltar                           ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                listarUsuarios();
                break;
            case 2:
                buscarPorId();
                break;
            case 3:
                buscarPorEmail();
                break;
            case 4:
                cadastrarUsuario();
                break;
            case 5:
                atualizarUsuario();
                break;
            case 6:
                removerUsuario();
                break;
            case 0:
                return false;
            default:
                System.out.println("\n✗ Opção inválida!");
                pausa();
        }
        return true;
    }

    private void listarUsuarios() {
        try {
            var usuarios = usuarioRepository.buscarTodos();
            System.out.println("\n👥 USUÁRIOS CADASTRADOS:");
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado.");
            } else {
                usuarios.forEach(u -> System.out.println(
                    "  • " + u.getNome() + " (" + u.getTipo() + ") - " + u.getEmail()
                ));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void buscarPorId() {
        System.out.print("\nDigite o ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            var usuario = usuarioRepository.buscarPorId(id);
            if (usuario.isPresent()) {
                System.out.println("\n✓ Usuário encontrado:");
                System.out.println("  Nome: " + usuario.get().getNome());
                System.out.println("  Tipo: " + usuario.get().getTipo());
                System.out.println("  Email: " + usuario.get().getEmail());
            } else {
                System.out.println("\n✗ Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void buscarPorEmail() {
        System.out.print("\nDigite o email: ");
        String email = scanner.nextLine().trim();
        try {
            var usuario = usuarioRepository.buscarPorEmail(email);
            if (usuario.isPresent()) {
                System.out.println("\n✓ Usuário encontrado:");
                System.out.println("  Nome: " + usuario.get().getNome());
                System.out.println("  Tipo: " + usuario.get().getTipo());
            } else {
                System.out.println("\n✗ Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void cadastrarUsuario() {
        System.out.print("\nNome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.println("Tipo (1-ALUNO, 2-PROFESSOR, 3-SERVIDOR):");
        try {
            int tipoInt = Integer.parseInt(scanner.nextLine().trim());
            br.edu.biblioteca.model.Usuario.TipoUsuario tipo = br.edu.biblioteca.model.Usuario.TipoUsuario.values()[tipoInt - 1];
            
            br.edu.biblioteca.model.Usuario usuario = new br.edu.biblioteca.model.Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTipo(tipo);
            usuarioRepository.salvar(usuario);
            System.out.println("\n✓ Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void atualizarUsuario() {
        System.out.print("\nDigite o ID do usuário: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine().trim();
            System.out.print("Novo email: ");
            String email = scanner.nextLine().trim();

            var usuario = usuarioRepository.buscarPorId(id);
            if (usuario.isPresent()) {
                var u = usuario.get();
                u.setNome(nome);
                u.setEmail(email);
                usuarioRepository.salvar(u);
                System.out.println("\n✓ Usuário atualizado com sucesso!");
            } else {
                System.out.println("\n✗ Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void removerUsuario() {
        System.out.print("\nDigite o ID do usuário a remover: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            usuarioRepository.remover(id);
            System.out.println("\n✓ Usuário removido com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void pausa() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }
}
