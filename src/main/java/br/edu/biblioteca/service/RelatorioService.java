package br.edu.biblioteca.service;

import br.edu.biblioteca.structures.Vetor;
import br.edu.biblioteca.structures.Matriz;
import br.edu.biblioteca.structures.ArvoreBST;
import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.model.Emprestimo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Serviço para gerar relatórios sobre a biblioteca.
 * Fornece análises de empréstimos, multas e estatísticas.
 */
public class RelatorioService {
    private CatalogoService catalogoService;
    private EmprestimoService emprestimoService;
    private UsuarioService usuarioService;
    private Matriz<Integer> emprestimosPorMesCategoria;

    public RelatorioService(CatalogoService catalogoService, 
                           EmprestimoService emprestimoService,
                           UsuarioService usuarioService) {
        this.catalogoService = catalogoService;
        this.emprestimoService = emprestimoService;
        this.usuarioService = usuarioService;
        this.emprestimosPorMesCategoria = new Matriz<>(12, 10);

        // Inicializar matriz com zeros
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                emprestimosPorMesCategoria.set(i, j, 0);
            }
        }
    }

    /**
     * Retorna os livros mais emprestados
     *
     * @param limite Número de livros a retornar
     * @return Vetor com livros mais emprestados
     */
    public Vetor<Livro> topMaisEmprestados(int limite) {
        Vetor<Livro> todos = catalogoService.listarTodos();
        Vetor<Livro> resultado = new Vetor<>();

        // Contar empréstimos por livro
        ArvoreBST<String, Integer> contagem = new ArvoreBST<>();

        for (Emprestimo emp : emprestimoService.listarTodos()) {
            Integer count = contagem.get(emp.getExemplarId().toString());
            if (count == null) {
                count = 0;
            }
            contagem.put(emp.getExemplarId().toString(), count + 1);
        }

        // Ordenar por contagem (simplificado)
        // Em produção, usar estrutura adequada para top-k
        int contador = 0;
        for (Livro livro : todos) {
            if (contador >= limite) {
                break;
            }
            Integer count = contagem.get(livro.getIsbn());
            if (count != null && count > 0) {
                resultado.add(livro);
                contador++;
            }
        }

        return resultado;
    }

    /**
     * Lista empréstimos em atraso
     *
     * @return Vetor com empréstimos atrasados
     */
    public Vetor<Emprestimo> emAtraso() {
        return emprestimoService.listarAtrasados();
    }

    /**
     * Retorna usuários com mais atrasos
     *
     * @param limite Número de usuários a retornar
     * @return Vetor com usuários com mais atrasos
     */
    public Vetor<Long> usuariosComMaisAtrasos(int limite) {
        Vetor<Emprestimo> atrasados = emAtraso();
        ArvoreBST<Long, Integer> contagemAtrasos = new ArvoreBST<>();

        // Contar atrasos por usuário
        for (Emprestimo emp : atrasados) {
            Long usuarioId = emp.getUsuarioId();
            Integer count = contagemAtrasos.get(usuarioId);
            if (count == null) {
                count = 0;
            }
            contagemAtrasos.put(usuarioId, count + 1);
        }

        // Extrair top usuários (simplificado)
        Vetor<Long> resultado = new Vetor<>();
        java.util.Iterator<Long> iter = contagemAtrasos.inOrder();
        int contador = 0;

        while (iter.hasNext() && contador < limite) {
            Long usuarioId = iter.next();
            resultado.add(usuarioId);
            contador++;
        }

        return resultado;
    }

    /**
     * Gera estatísticas mensais de empréstimos
     *
     * @return Matriz com dados [mês][categoria]
     */
    public Matriz<Integer> estatisticasMensais() {
        Vetor<Emprestimo> emprestimos = emprestimoService.listarTodos();
        Matriz<Integer> stats = new Matriz<>(12, 10);

        // Inicializar
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                stats.set(i, j, 0);
            }
        }

        // Preencher estatísticas
        for (Emprestimo emp : emprestimos) {
            if (emp.getDataEmprestimo() != null) {
                int mes = emp.getDataEmprestimo().getMonthValue() - 1;
                int categoria = 0; // Simplificado

                int valor = stats.get(mes, categoria);
                stats.set(mes, categoria, valor + 1);
            }
        }

        return stats;
    }

    /**
     * Calcula total de multas pendentes do sistema
     *
     * @return Valor total de multas
     */
    public double calcularMultasTotaisPendentes() {
        double total = 0.0;
        Vetor<Emprestimo> atrasados = emAtraso();

        for (Emprestimo emp : atrasados) {
            total += emprestimoService.calcularMulta(emp.getId());
        }

        return total;
    }

    /**
     * Calcula multas totais por usuário
     *
     * @return Vetor com IDs e valores de multas
     */
    public Vetor<String> calcularMultasPorUsuario() {
        Vetor<String> resultado = new Vetor<>();
        Vetor<Emprestimo> atrasados = emAtraso();
        ArvoreBST<Long, Double> multas = new ArvoreBST<>();

        // Calcular multas por usuário
        for (Emprestimo emp : atrasados) {
            Long usuarioId = emp.getUsuarioId();
            Double multa = multas.get(usuarioId);
            if (multa == null) {
                multa = 0.0;
            }
            multa += emprestimoService.calcularMulta(emp.getId());
            multas.put(usuarioId, multa);
        }

        // Formatar resultado
        java.util.Iterator<Long> iter = multas.inOrder();
        while (iter.hasNext()) {
            Long usuarioId = iter.next();
            Double multa = multas.get(usuarioId);
            resultado.add("Usuário " + usuarioId + ": R$ " + 
                         String.format("%.2f", multa));
        }

        return resultado;
    }

    /**
     * Gera relatório de livros mais emprestados
     *
     * @param limite Número de livros
     * @return String com relatório formatado
     */
    public String gerarRelatorioCincoMaisEmprestados(int limite) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TOP ").append(limite).append(" LIVROS MAIS EMPRESTADOS ===\n");

        Vetor<Livro> topLivros = topMaisEmprestados(limite);

        for (int i = 0; i < topLivros.size(); i++) {
            Livro livro = topLivros.get(i);
            sb.append((i + 1)).append(". ").append(livro.getTitulo())
              .append(" (").append(livro.getIsbn()).append(")\n");
        }

        return sb.toString();
    }

    /**
     * Gera relatório de empréstimos em atraso
     *
     * @return String com relatório formatado
     */
    public String gerarRelatorioEmAtraso() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== EMPRÉSTIMOS EM ATRASO ===\n");

        Vetor<Emprestimo> atrasados = emAtraso();
        sb.append("Total: ").append(atrasados.size()).append("\n\n");

        for (Emprestimo emp : atrasados) {
            long diasAtraso = ChronoUnit.DAYS.between(
                emp.getDataPrevista(), LocalDate.now());
            double multa = emprestimoService.calcularMulta(emp.getId());

            sb.append("Empréstimo ID: ").append(emp.getId()).append("\n")
              .append("  Usuário: ").append(emp.getUsuarioId()).append("\n")
              .append("  Dias em atraso: ").append(diasAtraso).append("\n")
              .append("  Multa: R$ ").append(String.format("%.2f", multa)).append("\n\n");
        }

        return sb.toString();
    }

    /**
     * Gera relatório de usuários com mais atrasos
     *
     * @param limite Número de usuários
     * @return String com relatório formatado
     */
    public String gerarRelatorioUsuariosComMaisAtrasos(int limite) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TOP ").append(limite).append(" USUÁRIOS COM MAIS ATRASOS ===\n");

        Vetor<Long> topUsuarios = usuariosComMaisAtrasos(limite);

        for (int i = 0; i < topUsuarios.size(); i++) {
            Long usuarioId = topUsuarios.get(i);
            int atrasos = 0;

            for (Emprestimo emp : emAtraso()) {
                if (emp.getUsuarioId().equals(usuarioId)) {
                    atrasos++;
                }
            }

            sb.append((i + 1)).append(". Usuário ").append(usuarioId)
              .append(": ").append(atrasos).append(" atrasos\n");
        }

        return sb.toString();
    }

    /**
     * Gera relatório geral de estatísticas
     *
     * @return String com relatório formatado
     */
    public String gerarRelatorioEstatisticasGerais() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTATÍSTICAS GERAIS DA BIBLIOTECA ===\n\n");

        sb.append("Total de Livros: ").append(catalogoService.getTotalLivros()).append("\n");
        sb.append("Total de Exemplares: ").append(catalogoService.getTotalExemplares()).append("\n");
        sb.append("Total de Usuários: ").append(usuarioService.getTotalUsuarios()).append("\n");
        sb.append("Total de Empréstimos Ativos: ")
          .append(emprestimoService.getTotalAtivos()).append("\n");
        sb.append("Total de Empréstimos Atrasados: ")
          .append(emprestimoService.getTotalAtrasados()).append("\n");
        sb.append("Multas Totais Pendentes: R$ ")
          .append(String.format("%.2f", calcularMultasTotaisPendentes())).append("\n");

        return sb.toString();
    }
}
