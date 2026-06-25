package br.edu.biblioteca.ui;

import java.util.Scanner;

/**
 * Menu principal da aplicação de biblioteca.
 */
public class MenuPrincipal {

    private Scanner scanner = new Scanner(System.in);
    private TelaCatalogo telaCatalogo;
    private TelaUsuarios telaUsuarios;
    private TelaEmprestimos telaEmprestimos;
    private TelaReservas telaReservas;
    private TelaRelatorios telaRelatorios;

    public MenuPrincipal() {
        this.telaCatalogo = new TelaCatalogo();
        this.telaUsuarios = new TelaUsuarios();
        this.telaEmprestimos = new TelaEmprestimos();
        this.telaReservas = new TelaReservas();
        this.telaRelatorios = new TelaRelatorios();
    }

    /**
     * Exibe o menu principal e executa ações.
     */
    public void exibir() {
        boolean ativo = true;
        while (ativo) {
            exibirOpcoes();
            int opcao = lerOpcao();
            ativo = executarOpcao(opcao);
        }
    }

    private void exibirOpcoes() {
        limparTela();
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE BIBLIOTECA v1.0       ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1 - Catálogo de Livros               ║");
        System.out.println("║ 2 - Gerenciar Usuários               ║");
        System.out.println("║ 3 - Empréstimos                      ║");
        System.out.println("║ 4 - Reservas                         ║");
        System.out.println("║ 5 - Relatórios                       ║");
        System.out.println("║ 0 - Sair                             ║");
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
                telaCatalogo.exibir();
                break;
            case 2:
                telaUsuarios.exibir();
                break;
            case 3:
                telaEmprestimos.exibir();
                break;
            case 4:
                telaReservas.exibir();
                break;
            case 5:
                telaRelatorios.exibir();
                break;
            case 0:
                System.out.println("\n✓ Encerrando aplicação...");
                return false;
            default:
                System.out.println("\n✗ Opção inválida!");
                pausa();
        }
        return true;
    }

    protected void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    protected void pausa() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.exibir();
    }
}
