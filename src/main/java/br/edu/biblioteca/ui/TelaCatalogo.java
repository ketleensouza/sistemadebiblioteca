package br.edu.biblioteca.ui;

import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.repository.LivroRepository;
import java.util.Scanner;

/**
 * Tela para gerenciar catálogo de livros.
 */
public class TelaCatalogo {

    private Scanner scanner = new Scanner(System.in);
    private LivroRepository livroRepository = new LivroRepository();

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
        System.out.println("║         CATÁLOGO DE LIVROS            ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1 - Listar Livros                    ║");
        System.out.println("║ 2 - Buscar por ISBN                  ║");
        System.out.println("║ 3 - Cadastrar Novo Livro             ║");
        System.out.println("║ 4 - Atualizar Livro                  ║");
        System.out.println("║ 5 - Remover Livro                    ║");
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
                listarLivros();
                break;
            case 2:
                buscarPorIsbn();
                break;
            case 3:
                cadastrarLivro();
                break;
            case 4:
                atualizarLivro();
                break;
            case 5:
                removerLivro();
                break;
            case 0:
                return false;
            default:
                System.out.println("\n✗ Opção inválida!");
                pausa();
        }
        return true;
    }

    private void listarLivros() {
        try {
            var livros = livroRepository.buscarTodos();
            System.out.println("\n📚 LIVROS CADASTRADOS:");
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro cadastrado.");
            } else {
                livros.forEach(l -> System.out.println("  • " + l.getTitulo() + " (ISBN: " + l.getIsbn() + ")"));
            }
        } catch (Exception e) {
            System.out.println("✗ Erro ao listar livros: " + e.getMessage());
        }
        pausa();
    }

    private void buscarPorIsbn() {
        System.out.print("\nDigite o ISBN: ");
        String isbn = scanner.nextLine().trim();
        try {
            var livro = livroRepository.buscarPorIsbn(isbn);
            if (livro.isPresent()) {
                System.out.println("\n✓ Livro encontrado:");
                System.out.println("  Título: " + livro.get().getTitulo());
                System.out.println("  Ano: " + livro.get().getAno());
            } else {
                System.out.println("\n✗ Livro não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
        pausa();
    }

    private void cadastrarLivro() {
        System.out.print("\nISBN: ");
        String isbn = scanner.nextLine().trim();
        
        if (isbn.isEmpty()) {
            System.out.println("✗ ISBN não pode estar vazio!");
            pausa();
            return;
        }
        
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();
        
        System.out.print("Ano de publicação: ");
        Integer ano = null;
        try {
            String anoStr = scanner.nextLine().trim();
            if (!anoStr.isEmpty()) {
                ano = Integer.parseInt(anoStr);
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️  Ano inválido, usando sem data.");
        }
        
        System.out.print("Palavras-chave (opcional): ");
        String palavrasChave = scanner.nextLine().trim();

        try {
            Livro livro = new Livro();
            livro.setIsbn(isbn);
            livro.setTitulo(titulo);
            if (ano != null) {
                livro.setAno(ano);
            }
            if (!palavrasChave.isEmpty()) {
                livro.setPalavrasChave(palavrasChave);
            }
            livroRepository.salvar(livro);
            System.out.println("\n✓ Livro cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro ao cadastrar: " + e.getMessage());
        }
        pausa();
    }

    private void atualizarLivro() {
        System.out.print("\nDigite o ISBN do livro: ");
        String isbn = scanner.nextLine().trim();
        
        try {
            var livroOpt = livroRepository.buscarPorIsbn(isbn);
            if (!livroOpt.isPresent()) {
                System.out.println("\n✗ Livro não encontrado.");
                pausa();
                return;
            }
            
            Livro livro = livroOpt.get();
            
            System.out.print("Novo título (atual: " + livro.getTitulo() + "): ");
            String titulo = scanner.nextLine().trim();
            if (!titulo.isEmpty()) {
                livro.setTitulo(titulo);
            }
            
            System.out.print("Novo ano (atual: " + livro.getAno() + "): ");
            try {
                String anoStr = scanner.nextLine().trim();
                if (!anoStr.isEmpty()) {
                    livro.setAno(Integer.parseInt(anoStr));
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Ano inválido, mantendo anterior.");
            }
            
            System.out.print("Palavras-chave (atual: " + (livro.getPalavrasChave() != null ? livro.getPalavrasChave() : "n/a") + "): ");
            String palavras = scanner.nextLine().trim();
            if (!palavras.isEmpty()) {
                livro.setPalavrasChave(palavras);
            }
            
            livroRepository.atualizar(livro);
            System.out.println("\n✓ Livro atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro ao atualizar: " + e.getMessage());
        }
        pausa();
    }

    private void removerLivro() {
        System.out.print("\nDigite o ISBN do livro a remover: ");
        String isbn = scanner.nextLine().trim();

        try {
            livroRepository.remover(isbn);
            System.out.println("\n✓ Livro removido com sucesso!");
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
