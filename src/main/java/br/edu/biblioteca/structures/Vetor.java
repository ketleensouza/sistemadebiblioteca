package br.edu.biblioteca.structures;

import java.util.Iterator;

/**
 * Implementação de um vetor dinâmico genérico.
 * Oferece operações de get, set, add e remove com redimensionamento automático.
 *
 * @param <T> Tipo dos elementos armazenados
 */
public class Vetor<T> implements Iterable<T> {
    private static final int CAPACIDADE_INICIAL = 10;
    private static final int FATOR_CRESCIMENTO = 2;

    private Object[] elementos;
    private int tamanho;

    public Vetor() {
        this.elementos = new Object[CAPACIDADE_INICIAL];
        this.tamanho = 0;
    }

    public Vetor(int capacidadeInicial) {
        this.elementos = new Object[capacidadeInicial];
        this.tamanho = 0;
    }

    /**
     * Retorna o número de elementos no vetor
     */
    public int size() {
        return tamanho;
    }

    /**
     * Verifica se o vetor está vazio
     */
    public boolean isEmpty() {
        return tamanho == 0;
    }

    /**
     * Retorna o elemento na posição indicada
     *
     * @param indice Índice do elemento (0-baseado)
     * @return Elemento na posição indice
     * @throws IndexOutOfBoundsException Se o índice for inválido
     */
    @SuppressWarnings("unchecked")
    public T get(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        return (T) elementos[indice];
    }

    /**
     * Define o elemento na posição indicada
     *
     * @param indice Índice onde inserir
     * @param elemento Elemento a ser inserido
     * @throws IndexOutOfBoundsException Se o índice for inválido
     */
    public void set(int indice, T elemento) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        elementos[indice] = elemento;
    }

    /**
     * Adiciona um elemento ao final do vetor
     *
     * @param elemento Elemento a adicionar
     */
    public void add(T elemento) {
        if (tamanho == elementos.length) {
            redimensionar(elementos.length * FATOR_CRESCIMENTO);
        }
        elementos[tamanho++] = elemento;
    }

    /**
     * Insere um elemento em uma posição específica
     *
     * @param indice Posição onde inserir
     * @param elemento Elemento a inserir
     * @throws IndexOutOfBoundsException Se o índice for inválido
     */
    public void add(int indice, T elemento) {
        if (indice < 0 || indice > tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }

        if (tamanho == elementos.length) {
            redimensionar(elementos.length * FATOR_CRESCIMENTO);
        }

        // Desloca elementos para a direita
        for (int i = tamanho; i > indice; i--) {
            elementos[i] = elementos[i - 1];
        }

        elementos[indice] = elemento;
        tamanho++;
    }

    /**
     * Remove e retorna o elemento na posição indicada
     *
     * @param indice Posição do elemento a remover
     * @return Elemento removido
     * @throws IndexOutOfBoundsException Se o índice for inválido
     */
    @SuppressWarnings("unchecked")
    public T remove(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }

        T elemento = (T) elementos[indice];

        // Desloca elementos para a esquerda
        for (int i = indice; i < tamanho - 1; i++) {
            elementos[i] = elementos[i + 1];
        }

        elementos[--tamanho] = null;

        // Redimensiona se necessário
        if (tamanho > 0 && tamanho < elementos.length / 4) {
            redimensionar(elementos.length / 2);
        }

        return elemento;
    }

    /**
     * Remove a primeira ocorrência do elemento
     *
     * @param elemento Elemento a remover
     * @return true se elemento foi removido, false caso contrário
     */
    public boolean remove(Object elemento) {
        for (int i = 0; i < tamanho; i++) {
            if ((elemento == null && elementos[i] == null) || 
                (elemento != null && elemento.equals(elementos[i]))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se o vetor contém o elemento
     *
     * @param elemento Elemento a procurar
     * @return true se contém o elemento
     */
    public boolean contains(Object elemento) {
        for (int i = 0; i < tamanho; i++) {
            if ((elemento == null && elementos[i] == null) || 
                (elemento != null && elemento.equals(elementos[i]))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Limpa o vetor
     */
    public void clear() {
        for (int i = 0; i < tamanho; i++) {
            elementos[i] = null;
        }
        tamanho = 0;
    }

    private void redimensionar(int novaCapacidade) {
        Object[] novoArray = new Object[novaCapacidade];
        System.arraycopy(elementos, 0, novoArray, 0, tamanho);
        elementos = novoArray;
    }

    @Override
    public Iterator<T> iterator() {
        return new VetorIterator();
    }

    private class VetorIterator implements Iterator<T> {
        private int indiceAtual = 0;

        @Override
        public boolean hasNext() {
            return indiceAtual < tamanho;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return (T) elementos[indiceAtual++];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamanho; i++) {
            sb.append(elementos[i]);
            if (i < tamanho - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
