package br.edu.biblioteca.service;

import br.edu.biblioteca.structures.Vetor;
import br.edu.biblioteca.structures.ArvoreBST;
import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.model.Exemplar;
import br.edu.biblioteca.model.Autor;
import br.edu.biblioteca.model.Categoria;

/**
 * Serviço para gerenciar o catálogo de livros e exemplares da biblioteca.
 * Responsável por cadastro, busca e listagem de livros e exemplares.
 */
public class CatalogoService {
    private ArvoreBST<String, Livro> livrosPorISBN;
    private ArvoreBST<String, Vetor<Exemplar>> exemplaresPorISBN;
    private Vetor<Livro> todoosLivros;

    public CatalogoService() {
        this.livrosPorISBN = new ArvoreBST<>();
        this.exemplaresPorISBN = new ArvoreBST<>();
        this.todoosLivros = new Vetor<>();
    }

    /**
     * Cadastra um novo livro no catálogo
     *
     * @param livro Livro a cadastrar
     * @return true se cadastrado com sucesso
     */
    public boolean cadastrarLivro(Livro livro) {
        if (livro == null || livro.getIsbn() == null) {
            throw new IllegalArgumentException("Livro e ISBN não podem ser nulos");
        }

        if (livrosPorISBN.containsKey(livro.getIsbn())) {
            return false; // Livro já existe
        }

        livrosPorISBN.put(livro.getIsbn(), livro);
        todoosLivros.add(livro);
        exemplaresPorISBN.put(livro.getIsbn(), new Vetor<>());

        return true;
    }

    /**
     * Cadastra um novo exemplar de um livro
     *
     * @param isbn ISBN do livro
     * @param exemplar Exemplar a cadastrar
     * @return true se cadastrado com sucesso
     */
    public boolean cadastrarExemplar(String isbn, Exemplar exemplar) {
        if (isbn == null || exemplar == null) {
            throw new IllegalArgumentException("ISBN e exemplar não podem ser nulos");
        }

        if (!livrosPorISBN.containsKey(isbn)) {
            return false; // Livro não existe
        }

        Vetor<Exemplar> exemplares = exemplaresPorISBN.get(isbn);
        exemplares.add(exemplar);
        return true;
    }

    /**
     * Remove um livro do catálogo
     *
     * @param isbn ISBN do livro a remover
     * @return true se removido com sucesso
     */
    public boolean removerLivro(String isbn) {
        if (isbn == null) {
            return false;
        }

        Livro removido = livrosPorISBN.remove(isbn);
        if (removido != null) {
            todoosLivros.remove(removido);
            exemplaresPorISBN.remove(isbn);
            return true;
        }
        return false;
    }

    /**
     * Remove um exemplar específico de um livro
     *
     * @param isbn ISBN do livro
     * @param exemplarId ID do exemplar
     * @return true se removido com sucesso
     */
    public boolean removerExemplar(String isbn, Long exemplarId) {
        if (isbn == null || exemplarId == null) {
            return false;
        }

        Vetor<Exemplar> exemplares = exemplaresPorISBN.get(isbn);
        if (exemplares != null) {
            for (int i = 0; i < exemplares.size(); i++) {
                if (exemplares.get(i).getId().equals(exemplarId)) {
                    exemplares.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Busca um livro por ISBN
     *
     * @param isbn ISBN a buscar
     * @return Livro encontrado ou null
     */
    public Livro buscarPorISBN(String isbn) {
        if (isbn == null) {
            return null;
        }
        return livrosPorISBN.get(isbn);
    }

    /**
     * Busca livros por título (busca parcial)
     *
     * @param titulo Título ou parte do título
     * @return Vetor com livros encontrados
     */
    public Vetor<Livro> buscarPorTitulo(String titulo) {
        Vetor<Livro> resultado = new Vetor<>();

        if (titulo == null || titulo.isEmpty()) {
            return resultado;
        }

        String tituloNorm = titulo.toLowerCase();

        for (Livro livro : todoosLivros) {
            if (livro.getTitulo() != null && 
                livro.getTitulo().toLowerCase().contains(tituloNorm)) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    /**
     * Busca livros por autor
     *
     * @param nomeAutor Nome do autor
     * @return Vetor com livros encontrados
     */
    public Vetor<Livro> buscarPorAutor(String nomeAutor) {
        Vetor<Livro> resultado = new Vetor<>();

        if (nomeAutor == null || nomeAutor.isEmpty()) {
            return resultado;
        }

        for (Livro livro : todoosLivros) {
            for (Autor autor : livro.getAutores()) {
                if (autor.getNome() != null && 
                    autor.getNome().equalsIgnoreCase(nomeAutor)) {
                    resultado.add(livro);
                    break;
                }
            }
        }

        return resultado;
    }

    /**
     * Busca livros por categoria
     *
     * @param nomeCategoria Nome da categoria
     * @return Vetor com livros encontrados
     */
    public Vetor<Livro> buscarPorCategoria(String nomeCategoria) {
        Vetor<Livro> resultado = new Vetor<>();

        if (nomeCategoria == null || nomeCategoria.isEmpty()) {
            return resultado;
        }

        for (Livro livro : todoosLivros) {
            for (Categoria categoria : livro.getCategorias()) {
                if (categoria.getNome() != null && 
                    categoria.getNome().equalsIgnoreCase(nomeCategoria)) {
                    resultado.add(livro);
                    break;
                }
            }
        }

        return resultado;
    }

    /**
     * Obtém todos os exemplares de um livro
     *
     * @param isbn ISBN do livro
     * @return Vetor com exemplares
     */
    public Vetor<Exemplar> obterExemplaresdoLivro(String isbn) {
        if (isbn == null) {
            return new Vetor<>();
        }

        Vetor<Exemplar> exemplares = exemplaresPorISBN.get(isbn);
        return exemplares != null ? exemplares : new Vetor<>();
    }

    /**
     * Conta exemplares disponíveis de um livro
     *
     * @param isbn ISBN do livro
     * @return Número de exemplares disponíveis
     */
    public int contarExemplaresDisponiveis(String isbn) {
        Vetor<Exemplar> exemplares = obterExemplaresdoLivro(isbn);
        int contador = 0;

        for (Exemplar exemplar : exemplares) {
            if (exemplar.estaDisponivel()) {
                contador++;
            }
        }

        return contador;
    }

    /**
     * Lista todos os livros do catálogo
     *
     * @return Vetor com todos os livros
     */
    public Vetor<Livro> listarTodos() {
        return todoosLivros;
    }

    /**
     * Lista todos os livros ordenados por ISBN
     *
     * @return Vetor com livros ordenados
     */
    public Vetor<Livro> listarPorISBN() {
        Vetor<Livro> resultado = new Vetor<>();
        java.util.Iterator<String> iter = livrosPorISBN.inOrder();

        while (iter.hasNext()) {
            String isbn = iter.next();
            resultado.add(livrosPorISBN.get(isbn));
        }

        return resultado;
    }

    /**
     * Retorna o número total de livros no catálogo
     *
     * @return Número de livros
     */
    public int getTotalLivros() {
        return livrosPorISBN.size();
    }

    /**
     * Retorna o número total de exemplares
     *
     * @return Número de exemplares
     */
    public int getTotalExemplares() {
        int total = 0;

        for (int i = 0; i < todoosLivros.size(); i++) {
            Livro livro = todoosLivros.get(i);
            Vetor<Exemplar> exemplares = exemplaresPorISBN.get(livro.getIsbn());
            if (exemplares != null) {
                total += exemplares.size();
            }
        }

        return total;
    }

    /**
     * Verifica se um livro existe no catálogo
     *
     * @param isbn ISBN a verificar
     * @return true se existe
     */
    public boolean existeLivro(String isbn) {
        return livrosPorISBN.containsKey(isbn);
    }

    /**
     * Obtém um exemplar específico
     *
     * @param isbn ISBN do livro
     * @param exemplarId ID do exemplar
     * @return Exemplar encontrado ou null
     */
    public Exemplar obterExemplar(String isbn, Long exemplarId) {
        Vetor<Exemplar> exemplares = obterExemplaresdoLivro(isbn);

        for (Exemplar exemplar : exemplares) {
            if (exemplar.getId().equals(exemplarId)) {
                return exemplar;
            }
        }

        return null;
    }

    /**
     * Limpa o catálogo
     */
    public void limpar() {
        livrosPorISBN = new ArvoreBST<>();
        exemplaresPorISBN = new ArvoreBST<>();
        todoosLivros.clear();
    }
}
