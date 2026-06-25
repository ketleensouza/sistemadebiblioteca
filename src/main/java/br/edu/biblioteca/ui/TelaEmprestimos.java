package br.edu.biblioteca.ui;

import br.edu.biblioteca.repository.EmprestimoRepository;
import java.util.Scanner;

/**
 * Tela para gerenciar empréstimos.
 */
public class TelaEmprestimos {

    private Scanner scanner = new Scanner(System.in);
    private EmprestimoRepository emprestimoRepository = new EmprestimoRepository();

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
        System.out.println("║         GERENCIAR EMPRÉSTIMOS         ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1 - Listar Empréstimos               ║");
        System.out.println("║ 2 - Buscar por ID                    ║");
        System.out.println("║ 3 - Buscar por Usuário               ║");
        System.out.println("║ 4 - Novo Empréstimo                  ║");
        System.out.println("║ 5 - Registrar Devolução              ║");
        System.out.println("║ 6 - Listar Atrasados                 ║");
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
                listarEmprestimos();
                break;
            case 2:
                buscarPorId();
                break;
            case 3:
                buscarPorUsuario();
                break;
            case 4:
                novoEmprestimo();
                break;
            case 5:
                registrarDevolucao();
                break;
            case 6:
                listarAtrasados();
                break;
            case 0:
                return false;
            default:
                System.out.println("\n✗ Opção inválida!");
                pausa();
        }
        return true;
    }

    private void listarEmprestimos() {
        try {
            var emprestimos = emprestimoRepository.buscarTodos();
            System.out.println("\n📋 EMPRÉSTIMOS REGISTRADOS:");
            if (emprestimos.isEmpty()) {
                System.out.println("Nenhum empréstimo registrado.");
            } else {
                emprestimos.forEach(e -> {
                    String status = e.estaAtrasado() ? "⚠️  ATRASADO" : "✓ Ativo";
                    System.out.println("  • ID: " + e.getId() + " | Usuário: " + e.getUsuarioId() +
                            " | Exemplar: " + e.getExemplarId() + " | " + status);
                });
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void buscarPorId() {
        System.out.print("\nDigite o ID do empréstimo: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            var emprestimo = emprestimoRepository.buscarPorId(id);
            if (emprestimo.isPresent()) {
                var e = emprestimo.get();
                System.out.println("\n✓ Empréstimo encontrado:");
                System.out.println("  Usuário ID: " + e.getUsuarioId());
                System.out.println("  Exemplar ID: " + e.getExemplarId());
                System.out.println("  Data Empréstimo: " + e.getDataEmprestimo());
                System.out.println("  Data Prevista: " + e.getDataPrevista());
                System.out.println("  Dias de Atraso: " + e.calcularDiasAtraso());
            } else {
                System.out.println("\n✗ Empréstimo não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void buscarPorUsuario() {
        System.out.print("\nDigite o ID do usuário: ");
        try {
            Long usuarioId = Long.parseLong(scanner.nextLine().trim());
            var emprestimos = emprestimoRepository.buscarPorUsuario(usuarioId);
            System.out.println("\n📋 EMPRÉSTIMOS DO USUÁRIO:");
            if (emprestimos.isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado.");
            } else {
                emprestimos.forEach(e -> System.out.println(
                    "  • ID: " + e.getId() + " | Exemplar: " + e.getExemplarId() +
                    " | Previsto: " + e.getDataPrevista()
                ));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void novoEmprestimo() {
        System.out.print("\nID do Usuário: ");
        try {
            Long usuarioId = Long.parseLong(scanner.nextLine().trim());
            System.out.print("ID do Exemplar: ");
            Long exemplarId = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Dias para devolução (ex: 14): ");
            int dias = Integer.parseInt(scanner.nextLine().trim());

            br.edu.biblioteca.model.Emprestimo emprestimo = new br.edu.biblioteca.model.Emprestimo();
            emprestimo.setUsuarioId(usuarioId);
            emprestimo.setExemplarId(exemplarId);
            emprestimo.setDataEmprestimo(java.time.LocalDate.now());
            emprestimo.setDataPrevista(java.time.LocalDate.now().plusDays(dias));
            emprestimoRepository.salvar(emprestimo);
            System.out.println("\n✓ Empréstimo registrado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void registrarDevolucao() {
        System.out.print("\nID do Empréstimo: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            var emprestimo = emprestimoRepository.buscarPorId(id);
            if (emprestimo.isPresent()) {
                var e = emprestimo.get();
                e.setDataDevolucao(java.time.LocalDate.now());
                emprestimoRepository.salvar(e);
                System.out.println("\n✓ Devolução registrada com sucesso!");
            } else {
                System.out.println("\n✗ Empréstimo não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void listarAtrasados() {
        try {
            var emprestimos = emprestimoRepository.buscarTodos();
            System.out.println("\n⚠️  EMPRÉSTIMOS ATRASADOS:");
            var atrasados = emprestimos.stream()
                    .filter(e -> e.estaAtrasado())
                    .toList();
            if (atrasados.isEmpty()) {
                System.out.println("Nenhum empréstimo atrasado.");
            } else {
                atrasados.forEach(e -> System.out.println(
                    "  • ID: " + e.getId() + " | Usuário: " + e.getUsuarioId() +
                    " | Atraso: " + e.calcularDiasAtraso() + " dias"
                ));
            }
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
