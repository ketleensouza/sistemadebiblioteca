package br.edu.biblioteca.action;

import br.edu.biblioteca.model.Reserva;

public class AcaoReservar implements Acao {
    private String isbnLivro;
    private Long usuarioId;
    private Reserva reserva;

    public AcaoReservar(String isbnLivro, Long usuarioId) {
        this.isbnLivro = isbnLivro;
        this.usuarioId = usuarioId;
    }

    @Override
    public void executar() {
        reserva = new Reserva();
        reserva.setIsbnLivro(isbnLivro);
        reserva.setUsuarioId(usuarioId);
        reserva.setStatus(Reserva.StatusReserva.RESERVADO);
    }

    @Override
    public void desfazer() {
        reserva = null;
    }

    @Override
    public String descricao() {
        return "Reserva do livro " + isbnLivro + " por usuário " + usuarioId;
    }
}
