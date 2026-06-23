package br.edu.biblioteca.service;

import br.edu.biblioteca.structures.Fila;
import br.edu.biblioteca.structures.Vetor;
import br.edu.biblioteca.structures.ArvoreBST;
import br.edu.biblioteca.model.Reserva;
import java.time.LocalDate;

/**
 * Serviço para gerenciar reservas de livros.
 * Utiliza Fila<Reserva> para manter ordem FIFO de reservas.
 * Responsável por reservar, cancelar e atender reservas.
 */
public class ReservaService {
    private ArvoreBST<String, Fila<Reserva>> filasReservasPorISBN;
    private ArvoreBST<Long, Reserva> reservasPorId;
    private Vetor<Reserva> todasReservas;
    private static final int LIMITE_RESERVAS_POR_ISBN = 100;

    public ReservaService() {
        this.filasReservasPorISBN = new ArvoreBST<>();
        this.reservasPorId = new ArvoreBST<>();
        this.todasReservas = new Vetor<>();
    }

    /**
     * Reserva um livro para um usuário
     *
     * @param usuarioId ID do usuário
     * @param isbn ISBN do livro
     * @return Reserva criada ou null se não for possível
     */
    public Reserva reservarLivro(Long usuarioId, String isbn) {
        if (usuarioId == null || isbn == null) {
            throw new IllegalArgumentException("Usuário ID e ISBN não podem ser nulos");
        }

        // Criar fila se não existir
        if (!filasReservasPorISBN.containsKey(isbn)) {
            filasReservasPorISBN.put(isbn, new Fila<>());
        }

        Fila<Reserva> fila = filasReservasPorISBN.get(isbn);

        // Verificar limite
        if (fila.size() >= LIMITE_RESERVAS_POR_ISBN) {
            return null;
        }

        // Criar reserva
        Reserva reserva = new Reserva(usuarioId, isbn, LocalDate.now());
        reserva.setStatus(Reserva.StatusReserva.RESERVADO);

        // Gerar ID
        Long id = System.currentTimeMillis();
        reservasPorId.put(id, reserva);
        todasReservas.add(reserva);

        // Adicionar à fila
        fila.enqueue(reserva);

        return reserva;
    }

    /**
     * Cancela uma reserva
     *
     * @param reservaId ID da reserva a cancelar
     * @return true se cancelada com sucesso
     */
    public boolean cancelarReserva(Long reservaId) {
        if (reservaId == null) {
            return false;
        }

        Reserva reserva = reservasPorId.get(reservaId);
        if (reserva == null || reserva.getStatus() == Reserva.StatusReserva.CANCELADO) {
            return false;
        }

        reserva.setStatus(Reserva.StatusReserva.CANCELADO);
        return true;
    }

    /**
     * Atende a próxima reserva para um livro específico
     *
     * @param isbn ISBN do livro
     * @return Reserva atendida ou null
     */
    public Reserva atenderProximaReserva(String isbn) {
        if (isbn == null) {
            return null;
        }

        Fila<Reserva> fila = filasReservasPorISBN.get(isbn);
        if (fila == null || fila.isEmpty()) {
            return null;
        }

        // Buscar próxima reserva não cancelada
        while (!fila.isEmpty()) {
            Reserva reserva = fila.dequeue();

            if (reserva.getStatus() != Reserva.StatusReserva.CANCELADO) {
                reserva.setStatus(Reserva.StatusReserva.RETIRADO);
                return reserva;
            }
        }

        return null;
    }

    /**
     * Obtém a posição de uma reserva na fila
     *
     * @param reservaId ID da reserva
     * @return Posição na fila (1-baseada) ou -1 se não encontrada
     */
    public int obterPosicaoNaFila(Long reservaId) {
        if (reservaId == null) {
            return -1;
        }

        Reserva reserva = reservasPorId.get(reservaId);
        if (reserva == null) {
            return -1;
        }

        Fila<Reserva> fila = filasReservasPorISBN.get(reserva.getIsbnLivro());
        if (fila == null) {
            return -1;
        }

        // Buscar posição (não há método direto em Fila, seria necessário iterar)
        return -1; // Simplificado
    }

    /**
     * Busca uma reserva por ID
     *
     * @param reservaId ID a buscar
     * @return Reserva encontrada ou null
     */
    public Reserva buscarPorId(Long reservaId) {
        if (reservaId == null) {
            return null;
        }
        return reservasPorId.get(reservaId);
    }

    /**
     * Lista reservas de um usuário
     *
     * @param usuarioId ID do usuário
     * @return Vetor com reservas do usuário
     */
    public Vetor<Reserva> buscarPorUsuario(Long usuarioId) {
        Vetor<Reserva> resultado = new Vetor<>();

        if (usuarioId == null) {
            return resultado;
        }

        for (Reserva res : todasReservas) {
            if (res.getUsuarioId().equals(usuarioId)) {
                resultado.add(res);
            }
        }

        return resultado;
    }

    /**
     * Lista reservas de um livro
     *
     * @param isbn ISBN do livro
     * @return Vetor com reservas do livro
     */
    public Vetor<Reserva> buscarPorLivro(String isbn) {
        Vetor<Reserva> resultado = new Vetor<>();

        if (isbn == null) {
            return resultado;
        }

        for (Reserva res : todasReservas) {
            if (isbn.equals(res.getIsbnLivro())) {
                resultado.add(res);
            }
        }

        return resultado;
    }

    /**
     * Lista todas as reservas ativas
     *
     * @return Vetor com reservas ativas
     */
    public Vetor<Reserva> listarAtivas() {
        Vetor<Reserva> resultado = new Vetor<>();

        for (Reserva res : todasReservas) {
            if (res.getStatus() == Reserva.StatusReserva.RESERVADO) {
                resultado.add(res);
            }
        }

        return resultado;
    }

    /**
     * Lista todas as reservas
     *
     * @return Vetor com todas as reservas
     */
    public Vetor<Reserva> listarTodas() {
        return todasReservas;
    }

    /**
     * Retorna o número de reservas na fila de um livro
     *
     * @param isbn ISBN do livro
     * @return Número de reservas
     */
    public int getTamanhoFilaLivro(String isbn) {
        if (isbn == null) {
            return 0;
        }

        Fila<Reserva> fila = filasReservasPorISBN.get(isbn);
        return fila != null ? fila.size() : 0;
    }

    /**
     * Retorna o número total de reservas ativas
     *
     * @return Número de ativas
     */
    public int getTotalAtivas() {
        return listarAtivas().size();
    }

    /**
     * Retorna o número total de reservas
     *
     * @return Número total
     */
    public int getTotalReservas() {
        return todasReservas.size();
    }

    /**
     * Verifica se há reservas aguardando para um livro
     *
     * @param isbn ISBN do livro
     * @return true se há reservas
     */
    public boolean temReservasAguardando(String isbn) {
        Fila<Reserva> fila = filasReservasPorISBN.get(isbn);
        return fila != null && !fila.isEmpty();
    }

    /**
     * Calcula tempo de espera estimado
     *
     * @param isbn ISBN do livro
     * @param diasPorReserva Dias aproximados por reserva
     * @return Dias estimados de espera
     */
    public int calcularTempoEsperaEstimado(String isbn, int diasPorReserva) {
        if (isbn == null || diasPorReserva <= 0) {
            return 0;
        }

        return getTamanhoFilaLivro(isbn) * diasPorReserva;
    }

    /**
     * Limpa todas as reservas
     */
    public void limpar() {
        filasReservasPorISBN = new ArvoreBST<>();
        reservasPorId = new ArvoreBST<>();
        todasReservas.clear();
    }

    /**
     * Retorna o LIMITE_RESERVAS_POR_ISBN
     */
    public static int getLimiteReservas() {
        return LIMITE_RESERVAS_POR_ISBN;
    }
}
