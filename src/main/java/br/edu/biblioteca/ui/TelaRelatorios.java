package br.edu.biblioteca.ui;

import br.edu.biblioteca.repository.*;
import java.util.Scanner;

/**
 * Tela para gerar relatórios do sistema.
 */
public class TelaRelatorios {

    private Scanner scanner = new Scanner(System.in);
    private LivroRepository livroRepository = new LivroRepository();
    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private EmprestimoRepository emprestimoRepository = new EmprestimoRepository();
    private ReservaRepository reservaRepository = new ReservaRepository();
    private ExemplarRepository exemplarRepository = new ExemplarRepository();

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
        System.out.println("║          RELATÓRIOS                  ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1 - Relatório de Livros              ║");
        System.out.println("║ 2 - Relatório de Usuários            ║");
        System.out.println("║ 3 - Relatório de Empréstimos Ativo   ║");
        System.out.println("║ 4 - Relatório de Atrasados           ║");
        System.out.println("║ 5 - Relatório de Reservas            ║");
        System.out.println("║ 6 - Estatísticas Gerais              ║");
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
                relatorioDeLivros();
                break;
            case 2:
                relatorioDeUsuarios();
                break;
            case 3:
                relatorioDeEmprestimosAtivos();
                break;
            case 4:
                relatorioDeAtrasados();
                break;
            case 5:
                relatorioDeReservas();
                break;
            case 6:
                estatisticasGerais();
                break;
            case 0:
                return false;
            default:
                System.out.println("\n✗ Opção inválida!");
                pausa();
        }
        return true;
    }

    private void relatorioDeLivros() {
        System.out.println("\n📚 RELATÓRIO DE LIVROS");
        System.out.println("═════════════════════════════════════");
        try {
            var livros = livroRepository.buscarTodos();
            System.out.println("Total de livros: " + livros.size());
            if (!livros.isEmpty()) {
                System.out.println("\nLivros cadastrados:");
                livros.forEach(l -> System.out.println(
                    "  • " + l.getTitulo() + " (ISBN: " + l.getIsbn() + ", Ano: " + l.getAno() + ")"
                ));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void relatorioDeUsuarios() {
        System.out.println("\n👥 RELATÓRIO DE USUÁRIOS");
        System.out.println("═════════════════════════════════════");
        try {
            var usuarios = usuarioRepository.buscarTodos();
            System.out.println("Total de usuários: " + usuarios.size());
            
            var alunos = usuarios.stream()
                    .filter(u -> u.getTipo() == br.edu.biblioteca.model.Usuario.TipoUsuario.ALUNO)
                    .count();
            var professores = usuarios.stream()
                    .filter(u -> u.getTipo() == br.edu.biblioteca.model.Usuario.TipoUsuario.PROFESSOR)
                    .count();
            var servidores = usuarios.stream()
                    .filter(u -> u.getTipo() == br.edu.biblioteca.model.Usuario.TipoUsuario.SERVIDOR)
                    .count();

            System.out.println("  • Alunos: " + alunos);
            System.out.println("  • Professores: " + professores);
            System.out.println("  • Servidores: " + servidores);
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void relatorioDeEmprestimosAtivos() {
        System.out.println("\n📋 RELATÓRIO DE EMPRÉSTIMOS ATIVOS");
        System.out.println("═════════════════════════════════════");
        try {
            var emprestimos = emprestimoRepository.buscarPorStatus("ATIVO");
            System.out.println("Total de empréstimos ativos: " + emprestimos.size());
            if (!emprestimos.isEmpty()) {
                emprestimos.forEach(e -> System.out.println(
                    "  • ID: " + e.getId() + " | Usuário: " + e.getUsuarioId() +
                    " | Exemplar: " + e.getExemplarId() +
                    " | Vencimento: " + e.getDataPrevista()
                ));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void relatorioDeAtrasados() {
        System.out.println("\n⚠️  RELATÓRIO DE EMPRÉSTIMOS ATRASADOS");
        System.out.println("═════════════════════════════════════");
        try {
            var emprestimos = emprestimoRepository.buscarTodos();
            var atrasados = emprestimos.stream()
                    .filter(e -> e.estaAtrasado())
                    .toList();
            
            System.out.println("Total de atrasados: " + atrasados.size());
            
            long diasTotalAtraso = atrasados.stream()
                    .mapToLong(e -> e.calcularDiasAtraso())
                    .sum();
            
            System.out.println("Dias de atraso (total): " + diasTotalAtraso);
            
            if (!atrasados.isEmpty()) {
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

    private void relatorioDeReservas() {
        System.out.println("\n📑 RELATÓRIO DE RESERVAS");
        System.out.println("═════════════════════════════════════");
        try {
            var reservas = reservaRepository.buscarTodos();
            System.out.println("Total de reservas: " + reservas.size());
            
            var vigentes = reservas.stream()
                    .filter(r -> r.estaVigente())
                    .count();
            var canceladas = reservas.stream()
                    .filter(r -> r.getStatus() == br.edu.biblioteca.model.Reserva.StatusReserva.CANCELADO)
                    .count();
            var retiradas = reservas.stream()
                    .filter(r -> r.getStatus() == br.edu.biblioteca.model.Reserva.StatusReserva.RETIRADO)
                    .count();

            System.out.println("  • Vigentes: " + vigentes);
            System.out.println("  • Canceladas: " + canceladas);
            System.out.println("  • Retiradas: " + retiradas);
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void estatisticasGerais() {
        System.out.println("\n📊 ESTATÍSTICAS GERAIS");
        System.out.println("═════════════════════════════════════");
        try {
            int livros = livroRepository.buscarTodos().size();
            int usuarios = usuarioRepository.buscarTodos().size();
            int exemplares = exemplarRepository.buscarTodos().size();
            int emprestimos = emprestimoRepository.buscarTodos().size();
            int reservas = reservaRepository.buscarTodos().size();

            System.out.println("📚 Livros: " + livros);
            System.out.println("👥 Usuários: " + usuarios);
            System.out.println("📖 Exemplares: " + exemplares);
            System.out.println("📋 Empréstimos: " + emprestimos);
            System.out.println("📑 Reservas: " + reservas);
            
            if (exemplares > 0) {
                System.out.println("\n📈 Taxa de ocupação: " +
                    String.format("%.1f%%", (emprestimos * 100.0 / exemplares)));
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
