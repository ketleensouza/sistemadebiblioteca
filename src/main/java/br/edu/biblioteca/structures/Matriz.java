package br.edu.biblioteca.structures;

import java.util.Iterator;

/**
 * Implementação de uma matriz (array 2D) genérica.
 * Útil para estatísticas por mês/categoria ou representação de dados tabulares.
 *
 * @param <T> Tipo dos elementos armazenados
 */
public class Matriz<T> implements Iterable<T> {
    private Object[][] elementos;
    private int linhas;
    private int colunas;

    /**
     * Cria uma matriz com número de linhas e colunas especificado
     *
     * @param linhas Número de linhas
     * @param colunas Número de colunas
     */
    public Matriz(int linhas, int colunas) {
        if (linhas <= 0 || colunas <= 0) {
            throw new IllegalArgumentException("Linhas e colunas devem ser maiores que zero");
        }
        this.elementos = new Object[linhas][colunas];
        this.linhas = linhas;
        this.colunas = colunas;
    }

    /**
     * Retorna o número de linhas
     *
     * @return Número de linhas
     */
    public int getLinhas() {
        return linhas;
    }

    /**
     * Retorna o número de colunas
     *
     * @return Número de colunas
     */
    public int getColunas() {
        return colunas;
    }

    /**
     * Retorna o elemento na posição (linha, coluna)
     *
     * @param linha Linha do elemento
     * @param coluna Coluna do elemento
     * @return Elemento na posição
     * @throws IndexOutOfBoundsException Se índices for inválidos
     */
    @SuppressWarnings("unchecked")
    public T get(int linha, int coluna) {
        validarIndices(linha, coluna);
        return (T) elementos[linha][coluna];
    }

    /**
     * Define o elemento na posição (linha, coluna)
     *
     * @param linha Linha do elemento
     * @param coluna Coluna do elemento
     * @param elemento Elemento a inserir
     * @throws IndexOutOfBoundsException Se índices for inválidos
     */
    public void set(int linha, int coluna, T elemento) {
        validarIndices(linha, coluna);
        elementos[linha][coluna] = elemento;
    }

    /**
     * Retorna uma linha inteira como um Vetor
     *
     * @param linha Número da linha
     * @return Vetor contendo os elementos da linha
     * @throws IndexOutOfBoundsException Se a linha for inválida
     */
    public Vetor<T> getLinha(int linha) {
        if (linha < 0 || linha >= linhas) {
            throw new IndexOutOfBoundsException("Linha inválida: " + linha);
        }

        Vetor<T> vetor = new Vetor<>();
        for (int j = 0; j < colunas; j++) {
            @SuppressWarnings("unchecked")
            T elemento = (T) elementos[linha][j];
            vetor.add(elemento);
        }
        return vetor;
    }

    /**
     * Retorna uma coluna inteira como um Vetor
     *
     * @param coluna Número da coluna
     * @return Vetor contendo os elementos da coluna
     * @throws IndexOutOfBoundsException Se a coluna for inválida
     */
    public Vetor<T> getColuna(int coluna) {
        if (coluna < 0 || coluna >= colunas) {
            throw new IndexOutOfBoundsException("Coluna inválida: " + coluna);
        }

        Vetor<T> vetor = new Vetor<>();
        for (int i = 0; i < linhas; i++) {
            @SuppressWarnings("unchecked")
            T elemento = (T) elementos[i][coluna];
            vetor.add(elemento);
        }
        return vetor;
    }

    /**
     * Limpa a matriz (define todos os elementos como null)
     */
    public void clear() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                elementos[i][j] = null;
            }
        }
    }

    /**
     * Transpõe a matriz
     *
     * @return Nova matriz transposta
     */
    public Matriz<T> transpor() {
        Matriz<T> transposta = new Matriz<>(colunas, linhas);
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                transposta.set(j, i, get(i, j));
            }
        }
        return transposta;
    }

    private void validarIndices(int linha, int coluna) {
        if (linha < 0 || linha >= linhas || coluna < 0 || coluna >= colunas) {
            throw new IndexOutOfBoundsException(
                    String.format("Índices inválidos: [%d, %d]. Válido: [0-%d, 0-%d]",
                            linha, coluna, linhas - 1, colunas - 1)
            );
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new MatrizIterator();
    }

    private class MatrizIterator implements Iterator<T> {
        private int linha = 0;
        private int coluna = 0;

        @Override
        public boolean hasNext() {
            return linha < Matriz.this.linhas;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            T elemento = (T) elementos[linha][coluna];

            coluna++;
            if (coluna >= Matriz.this.colunas) {
                coluna = 0;
                linha++;
            }

            return elemento;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linhas; i++) {
            sb.append("[");
            for (int j = 0; j < colunas; j++) {
                sb.append(elementos[i][j]);
                if (j < colunas - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
