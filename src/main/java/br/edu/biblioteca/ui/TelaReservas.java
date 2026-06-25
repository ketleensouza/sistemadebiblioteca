package br.edu.biblioteca.ui;

import br.edu.biblioteca.repository.ReservaRepository;
import java.util.Scanner;

/**
 * Tela para gerenciar reservas.
 */
public class TelaReservas {

    private Scanner scanner = new Scanner(System.in);
    private ReservaRepository reservaRepository = new ReservaRepository();

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
        System.out.println("║         GERENCIAR RESERVAS            ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1 - Listar Reservas                  ║");
        System.out.println("║ 2 - Buscar por ID                    ║");
        System.out.println("║ 3 - Buscar por Usuário               ║");
        System.out.println("║ 4 - Buscar por Livro (ISBN)          ║");
        System.out.println("║ 5 - Fazer Nova Reserva               ║");
        System.out.println("║ 6 - Cancelar Reserva                 ║");
        System.out.println("║ 7 - Listar Vigentes                  ║");
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
                listarReservas();
                break;
            case 2:
                buscarPorId();
                break;
            case 3:
                buscarPorUsuario();
                break;
            case 4:
                buscarPorIsbn();
                break;
            case 5:
                fazerReserva();
                break;
            case 6:
                cancelarReserva();
                break;
            case 7:
                listarVigentes();
                break;
            case 0:
                return false;
            default:
                System.out.println("\n✗ Opção inválida!");
                pausa();
        }
        return true;
    }

    private void listarReservas() {
        try {
            var reservas = reservaRepository.buscarTodos();
            System.out.println("\n📑 RESERVAS REGISTRADAS:");
            if (reservas.isEmpty()) {
                System.out.println("Nenhuma reserva registrada.");
            } else {
                reservas.forEach(r -> System.out.println(
                    "  • ID: " + r.getId() + " | Usuário: " + r.getUsuarioId() +
                    " | ISBN: " + r.getIsbnLivro() + " | " + r.getStatus()
                ));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void buscarPorId() {
        System.out.print("\nDigite o ID da reserva: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            var reserva = reservaRepository.buscarPorId(id);
            if (reserva.isPresent()) {
                var r = reserva.get();
                System.out.println("\n✓ Reserva encontrada:");
                System.out.println("  Usuário ID: " + r.getUsuarioId());
                System.out.println("  ISBN: " + r.getIsbnLivro());
                System.out.println("  Data: " + r.getDataReserva());
                System.out.println("  Status: " + r.getStatus());
            } else {
                System.out.println("\n✗ Reserva não encontrada.");
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
            var reservas = reservaRepository.buscarPorUsuario(usuarioId);
            System.out.println("\n📑 RESERVAS DO USUÁRIO:");
            if (reservas.isEmpty()) {
                System.out.println("Nenhuma reserva encontrada.");
            } else {
                reservas.forEach(r -> System.out.println(
                    "  • ID: " + r.getId() + " | ISBN: " + r.getIsbnLivro() +
                    " | " + r.getStatus()
                ));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void buscarPorIsbn() {
        System.out.print("\nDigite o ISBN do livro: ");
        String isbn = scanner.nextLine().trim();
        try {
            var reservas = reservaRepository.buscarPorIsbn(isbn);
            System.out.println("\n📑 RESERVAS PARA ESTE LIVRO:");
            if (reservas.isEmpty()) {
                System.out.println("Nenhuma reserva encontrada.");
            } else {
                reservas.forEach(r -> System.out.println(
                    "  • ID: " + r.getId() + " | Usuário: " + r.getUsuarioId() +
                    " | " + r.getStatus()
                ));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void fazerReserva() {
        System.out.print("\nID do Usuário: ");
        try {
            Long usuarioId = Long.parseLong(scanner.nextLine().trim());
            System.out.print("ISBN do Livro: ");
            String isbn = scanner.nextLine().trim();

            br.edu.biblioteca.model.Reserva reserva = new br.edu.biblioteca.model.Reserva();
            reserva.setUsuarioId(usuarioId);
            reserva.setIsbnLivro(isbn);
            reserva.setDataReserva(java.time.LocalDate.now());
            reserva.setStatus(br.edu.biblioteca.model.Reserva.StatusReserva.RESERVADO);
            
            reservaRepository.salvar(reserva);
            System.out.println("\n✓ Reserva realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void cancelarReserva() {
        System.out.print("\nID da Reserva a cancelar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            reservaRepository.remover(id);
            System.out.println("\n✓ Reserva cancelada com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void listarVigentes() {
        try {
            var reservas = reservaRepository.buscarPorStatus(
                br.edu.biblioteca.model.Reserva.StatusReserva.RESERVADO
            );
            System.out.println("\n✓ RESERVAS VIGENTES:");
            if (reservas.isEmpty()) {
                System.out.println("Nenhuma reserva vigente.");
            } else {
                reservas.forEach(r -> System.out.println(
                    "  • ID: " + r.getId() + " | Usuário: " + r.getUsuarioId() +
                    " | ISBN: " + r.getIsbnLivro()
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
