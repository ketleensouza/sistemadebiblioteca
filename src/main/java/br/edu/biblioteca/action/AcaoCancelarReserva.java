package br.edu.biblioteca.action;

import br.edu.biblioteca.model.Reserva;

public class AcaoCancelarReserva implements Acao {
    private Reserva reserva;
    private Reserva.StatusReserva statusAnterior;

    public AcaoCancelarReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public void executar() {
        this.statusAnterior = reserva.getStatus();
        reserva.setStatus(Reserva.StatusReserva.CANCELADO);
    }

    @Override
    public void desfazer() {
        reserva.setStatus(statusAnterior);
    }

    @Override
    public String descricao() {
        return "Cancelamento da reserva " + reserva.getId() + " do livro " + reserva.getIsbnLivro();
    }
}
