package br.edu.biblioteca.action;

import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.service.CatalogoService;

public class AcaoCadastrarLivro implements Acao {
    private Livro livro;
    private CatalogoService catalogoService;

    public AcaoCadastrarLivro(Livro livro, CatalogoService catalogoService) {
        this.livro = livro;
        this.catalogoService = catalogoService;
    }

    @Override
    public void executar() {
        catalogoService.cadastrarLivro(livro);
    }

    @Override
    public void desfazer() {
        catalogoService.removerLivro(livro.getIsbn());
    }

    @Override
    public String descricao() {
        return "Cadastro do livro " + livro.getTitulo();
    }
}
