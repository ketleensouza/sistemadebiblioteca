package br.edu.biblioteca.action;

public interface Acao {
    void executar();
    void desfazer();
    String descricao();
}
