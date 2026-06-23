package br.edu.biblioteca.service;

import br.edu.biblioteca.structures.Vetor;
import br.edu.biblioteca.structures.ArvoreBST;
import br.edu.biblioteca.model.Emprestimo;
import br.edu.biblioteca.model.Exemplar;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Serviço para gerenciar empréstimos de exemplares.
 * Responsável por emprestar, devolver, renovar e calcular multas.
 */
public class EmprestimoService {
    private ArvoreBST<Long, Emprestimo> emprestimosPorId;
    private Vetor<Emprestimo> todosEmprestimos;
    private static final int DIAS_EMPRESTIMO = 14;
    private static final double VALOR_MULTA_POR_DIA = 2.0;

    public EmprestimoService() {
        this.emprestimosPorId = new ArvoreBST<>();
        this.todosEmprestimos = new Vetor<>();
    }

    /**
     * Realiza um empréstimo de um exemplar
     *
     * @param usuarioId ID do usuário
     * @param exemplarId ID do exemplar
     * @return Empréstimo criado ou null se não for possível
     */
    public Emprestimo emprestarExemplar(Long usuarioId, Long exemplarId, 
                                        Exemplar exemplar, UsuarioService usuarioService) {
        if (usuarioId == null || exemplarId == null || exemplar == null) {
            throw new IllegalArgumentException("Parâmetros não podem ser nulos");
        }

        // Validar usuário
        if (!usuarioService.existeId(usuarioId)) {
            return null;
        }

        // Validar se usuário está bloqueado
        if (usuarioService.estaUsuarioBloqueado(usuarioId)) {
            return null;
        }

        // Validar se exemplar está disponível
        if (!exemplar.estaDisponivel()) {
            return null;
        }

        // Criar empréstimo
        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevista = hoje.plusDays(DIAS_EMPRESTIMO);

        Emprestimo emprestimo = new Emprestimo(usuarioId, exemplarId, hoje, dataPrevista);
        emprestimo.setStatus("ATIVO");

        // Gerar ID
        Long id = System.currentTimeMillis();
        emprestimosPorId.put(id, emprestimo);
        todosEmprestimos.add(emprestimo);

        // Atualizar status do exemplar
        exemplar.setStatus(Exemplar.StatusExemplar.EMPRESTADO);

        return emprestimo;
    }

    /**
     * Devolve um exemplar emprestado
     *
     * @param emprestimoId ID do empréstimo
     * @param exemplar Exemplar a devolver
     * @return Empréstimo finalizado ou null
     */
    public Emprestimo devolverExemplar(Long emprestimoId, Exemplar exemplar) {
        if (emprestimoId == null || exemplar == null) {
            throw new IllegalArgumentException("Parâmetros não podem ser nulos");
        }

        Emprestimo emprestimo = emprestimosPorId.get(emprestimoId);
        if (emprestimo == null || !emprestimo.getStatus().equals("ATIVO")) {
            return null;
        }

        // Atualizar empréstimo
        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus("FINALIZADO");

        // Atualizar exemplar
        exemplar.setStatus(Exemplar.StatusExemplar.DISPONIVEL);

        return emprestimo;
    }

    /**
     * Renova um empréstimo por mais dias
     *
     * @param emprestimoId ID do empréstimo
     * @param diasAdicionais Dias adicionais
     * @return true se renovado com sucesso
     */
    public boolean renovarEmprestimo(Long emprestimoId, int diasAdicionais) {
        if (emprestimoId == null || diasAdicionais <= 0) {
            return false;
        }

        Emprestimo emprestimo = emprestimosPorId.get(emprestimoId);
        if (emprestimo == null || !emprestimo.getStatus().equals("ATIVO")) {
            return false;
        }

        LocalDate novaDataPrevista = emprestimo.getDataPrevista().plusDays(diasAdicionais);
        emprestimo.setDataPrevista(novaDataPrevista);

        return true;
    }

    /**
     * Calcula a multa por atraso de um empréstimo
     *
     * @param emprestimoId ID do empréstimo
     * @return Valor da multa
     */
    public double calcularMulta(Long emprestimoId) {
        if (emprestimoId == null) {
            return 0.0;
        }

        Emprestimo emprestimo = emprestimosPorId.get(emprestimoId);
        if (emprestimo == null) {
            return 0.0;
        }

        LocalDate dataAtual = LocalDate.now();
        LocalDate dataPrevista = emprestimo.getDataPrevista();

        // Se ainda não venceu, sem multa
        if (dataAtual.isBefore(dataPrevista) || dataAtual.isEqual(dataPrevista)) {
            return 0.0;
        }

        // Calcular dias em atraso
        long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataAtual);
        return diasAtraso * VALOR_MULTA_POR_DIA;
    }

    /**
     * Busca um empréstimo por ID
     *
     * @param emprestimoId ID a buscar
     * @return Empréstimo encontrado ou null
     */
    public Emprestimo buscarPorId(Long emprestimoId) {
        if (emprestimoId == null) {
            return null;
        }
        return emprestimosPorId.get(emprestimoId);
    }

    /**
     * Lista empréstimos de um usuário
     *
     * @param usuarioId ID do usuário
     * @return Vetor com empréstimos do usuário
     */
    public Vetor<Emprestimo> buscarPorUsuario(Long usuarioId) {
        Vetor<Emprestimo> resultado = new Vetor<>();

        if (usuarioId == null) {
            return resultado;
        }

        for (Emprestimo emp : todosEmprestimos) {
            if (emp.getUsuarioId().equals(usuarioId)) {
                resultado.add(emp);
            }
        }

        return resultado;
    }

    /**
     * Lista empréstimos ativos
     *
     * @return Vetor com empréstimos ativos
     */
    public Vetor<Emprestimo> listarAtivos() {
        Vetor<Emprestimo> resultado = new Vetor<>();

        for (Emprestimo emp : todosEmprestimos) {
            if ("ATIVO".equals(emp.getStatus())) {
                resultado.add(emp);
            }
        }

        return resultado;
    }

    /**
     * Lista empréstimos em atraso
     *
     * @return Vetor com empréstimos atrasados
     */
    public Vetor<Emprestimo> listarAtrasados() {
        Vetor<Emprestimo> resultado = new Vetor<>();
        LocalDate hoje = LocalDate.now();

        for (Emprestimo emp : todosEmprestimos) {
            if ("ATIVO".equals(emp.getStatus()) && 
                emp.getDataPrevista().isBefore(hoje)) {
                resultado.add(emp);
            }
        }

        return resultado;
    }

    /**
     * Lista todos os empréstimos
     *
     * @return Vetor com todos os empréstimos
     */
    public Vetor<Emprestimo> listarTodos() {
        return todosEmprestimos;
    }

    /**
     * Retorna o número de empréstimos ativos
     *
     * @return Número de ativos
     */
    public int getTotalAtivos() {
        return listarAtivos().size();
    }

    /**
     * Retorna o número de empréstimos atrasados
     *
     * @return Número de atrasados
     */
    public int getTotalAtrasados() {
        return listarAtrasados().size();
    }

    /**
     * Retorna o número total de empréstimos
     *
     * @return Número total
     */
    public int getTotalEmprestimos() {
        return todosEmprestimos.size();
    }

    /**
     * Calcula multa total de um usuário
     *
     * @param usuarioId ID do usuário
     * @return Multa total
     */
    public double calcularMultaTotalUsuario(Long usuarioId) {
        double multa = 0.0;
        Vetor<Emprestimo> emprestimos = buscarPorUsuario(usuarioId);

        for (Emprestimo emp : emprestimos) {
            multa += calcularMulta(emp.getId());
        }

        return multa;
    }

    /**
     * Obtém dias disponíveis para renovação
     *
     * @param emprestimoId ID do empréstimo
     * @return Dias até vencimento
     */
    public long obterDiasDisponiveis(Long emprestimoId) {
        Emprestimo emprestimo = buscarPorId(emprestimoId);
        if (emprestimo == null) {
            return 0;
        }

        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, emprestimo.getDataPrevista());
    }

    /**
     * Limpa todos os empréstimos
     */
    public void limpar() {
        emprestimosPorId = new ArvoreBST<>();
        todosEmprestimos.clear();
    }

    /**
     * Retorna o DIAS_EMPRESTIMO padrão
     */
    public static int getDiasEmprestimo() {
        return DIAS_EMPRESTIMO;
    }
}
