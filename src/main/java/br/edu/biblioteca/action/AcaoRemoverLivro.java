package br.edu.biblioteca.action;

import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.service.CatalogoService;

public class AcaoRemoverLivro implements Acao {
    private Livro livro;
    private CatalogoService catalogoService;

    public AcaoRemoverLivro(Livro livro, CatalogoService catalogoService) {
        this.livro = livro;
        this.catalogoService = catalogoService;
    }

    @Override
    public void executar() {
        catalogoService.removerLivro(livro.getIsbn());
    }

    @Override
    public void desfazer() {
        catalogoService.cadastrarLivro(livro);
    }

    @Override
    public String descricao() {
        return "Remoção do livro " + livro.getTitulo();
    }
}
