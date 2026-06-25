package br.edu.biblioteca.repository;

import br.edu.biblioteca.model.Reserva;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Repository para persistência de Reservas em CSV.
 */
public class ReservaRepository {

    private static final String ARQUIVO = "reservas";
    private static final String CABECALHO = "id,usuarioId,isbnLivro,dataReserva,status";
    private static long proximoId = 1;

    static {
        carregarProximoId();
    }

    /**
     * Salva uma reserva no repositório.
     */
    public void salvar(Reserva reserva) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        if (linhas.isEmpty()) {
            linhas.add(CABECALHO);
        }

        if (reserva.getId() == null) {
            reserva.setId(proximoId++);
        }

        String linha = FileStorage.converterParaCSV(
                reserva.getId(),
                reserva.getUsuarioId(),
                reserva.getIsbnLivro(),
                reserva.getDataReserva(),
                reserva.getStatus().name()
        );

        linhas.add(linha);
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Carrega uma reserva pelo ID.
     */
    public Optional<Reserva> buscarPorId(Long id) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (Long.parseLong(valores.get(0)) == id) {
                return Optional.of(construirReserva(valores));
            }
        }
        return Optional.empty();
    }

    /**
     * Carrega reservas por usuário.
     */
    public List<Reserva> buscarPorUsuario(Long usuarioId) throws IOException {
        List<Reserva> reservas = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (Long.parseLong(valores.get(1)) == usuarioId) {
                reservas.add(construirReserva(valores));
            }
        }
        return reservas;
    }

    /**
     * Carrega reservas por ISBN do livro.
     */
    public List<Reserva> buscarPorIsbn(String isbn) throws IOException {
        List<Reserva> reservas = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (valores.get(2).equals(isbn)) {
                reservas.add(construirReserva(valores));
            }
        }
        return reservas;
    }

    /**
     * Carrega reservas por status.
     */
    public List<Reserva> buscarPorStatus(Reserva.StatusReserva status) throws IOException {
        List<Reserva> reservas = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linha);
            if (valores.get(4).equals(status.name())) {
                reservas.add(construirReserva(valores));
            }
        }
        return reservas;
    }

    /**
     * Carrega todas as reservas.
     */
    public List<Reserva> buscarTodos() throws IOException {
        List<Reserva> reservas = new ArrayList<>();
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (String linha : linhas) {
            if (linha.equals(CABECALHO)) continue;
            reservas.add(construirReserva(FileStorage.dividirCSV(linha)));
        }
        return reservas;
    }

    /**
     * Atualiza uma reserva.
     */
    public void atualizar(Reserva reserva) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i).equals(CABECALHO)) continue;
            List<String> valores = FileStorage.dividirCSV(linhas.get(i));
            if (Long.parseLong(valores.get(0)) == reserva.getId()) {
                String novaLinha = FileStorage.converterParaCSV(
                        reserva.getId(),
                        reserva.getUsuarioId(),
                        reserva.getIsbnLivro(),
                        reserva.getDataReserva(),
                        reserva.getStatus().name()
                );
                linhas.set(i, novaLinha);
                break;
            }
        }
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    /**
     * Remove uma reserva.
     */
    public void remover(Long id) throws IOException {
        List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
        linhas.removeIf(linha -> {
            if (linha.equals(CABECALHO)) return false;
            List<String> valores = FileStorage.dividirCSV(linha);
            return Long.parseLong(valores.get(0)) == id;
        });
        FileStorage.salvarCSV(ARQUIVO, linhas);
    }

    private Reserva construirReserva(List<String> valores) {
        Reserva reserva = new Reserva();
        try {
            if (!valores.isEmpty() && !valores.get(0).isEmpty()) {
                reserva.setId(Long.parseLong(valores.get(0)));
            }
            if (valores.size() > 1 && !valores.get(1).isEmpty()) {
                reserva.setUsuarioId(Long.parseLong(valores.get(1)));
            }
            if (valores.size() > 2 && !valores.get(2).isEmpty()) {
                reserva.setIsbnLivro(valores.get(2));
            }
            if (valores.size() > 3 && !valores.get(3).isEmpty()) {
                reserva.setDataReserva(LocalDate.parse(valores.get(3)));
            }
            if (valores.size() > 4 && !valores.get(4).isEmpty()) {
                try {
                    reserva.setStatus(Reserva.StatusReserva.valueOf(valores.get(4)));
                } catch (IllegalArgumentException e) {
                    reserva.setStatus(Reserva.StatusReserva.RESERVADO);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao construir reserva: " + e.getMessage());
        }
        return reserva;
    }

    private static void carregarProximoId() {
        try {
            List<String> linhas = FileStorage.carregarCSV(ARQUIVO);
            for (String linha : linhas) {
                if (linha.equals(CABECALHO)) continue;
                List<String> valores = FileStorage.dividirCSV(linha);
                long id = Long.parseLong(valores.get(0));
                if (id >= proximoId) {
                    proximoId = id + 1;
                }
            }
        } catch (IOException e) {
            proximoId = 1;
        }
    }
}
