package br.edu.biblioteca.structures;

import java.util.Iterator;

/**
 * Implementação de uma fila (queue) genérica - FIFO (First In First Out).
 * Útil para gerenciar reservas e lista de espera de exemplares.
 *
 * @param <T> Tipo dos elementos armazenados
 */
public class Fila<T> implements Iterable<T> {
    private static final int CAPACIDADE_INICIAL = 10;

    private Object[] elementos;
    private int inicio;
    private int fim;
    private int tamanho;

    public Fila() {
        this.elementos = new Object[CAPACIDADE_INICIAL];
        this.inicio = 0;
        this.fim = -1;
        this.tamanho = 0;
    }

    /**
     * Adiciona um elemento ao final da fila
     *
     * @param elemento Elemento a adicionar
     */
    public void enqueue(T elemento) {
        if (tamanho == elementos.length) {
            redimensionar(elementos.length * 2);
        }

        fim = (fim + 1) % elementos.length;
        elementos[fim] = elemento;
        tamanho++;
    }

    /**
     * Remove e retorna o primeiro elemento da fila
     *
     * @return Primeiro elemento
     * @throws IllegalStateException Se a fila estiver vazia
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Fila vazia");
        }

        Object elemento = elementos[inicio];
        elementos[inicio] = null;
        inicio = (inicio + 1) % elementos.length;
        tamanho--;

        // Redimensiona se necessário
        if (tamanho > 0 && tamanho < elementos.length / 4) {
            redimensionar(elementos.length / 2);
        }

        return (T) elemento;
    }

    /**
     * Retorna o primeiro elemento sem remover
     *
     * @return Primeiro elemento
     * @throws IllegalStateException Se a fila estiver vazia
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Fila vazia");
        }
        return (T) elementos[inicio];
    }

    /**
     * Verifica se a fila está vazia
     *
     * @return true se vazia
     */
    public boolean isEmpty() {
        return tamanho == 0;
    }

    /**
     * Retorna o número de elementos na fila
     *
     * @return Tamanho da fila
     */
    public int size() {
        return tamanho;
    }

    /**
     * Remove todos os elementos da fila
     */
    public void clear() {
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = null;
        }
        inicio = 0;
        fim = -1;
        tamanho = 0;
    }

    private void redimensionar(int novaCapacidade) {
        Object[] novoArray = new Object[novaCapacidade];
        for (int i = 0; i < tamanho; i++) {
            novoArray[i] = elementos[(inicio + i) % elementos.length];
        }
        elementos = novoArray;
        inicio = 0;
        fim = tamanho - 1;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilaIterator();
    }

    private class FilaIterator implements Iterator<T> {
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
            return (T) elementos[(inicio + indiceAtual++) % elementos.length];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamanho; i++) {
            sb.append(elementos[(inicio + i) % elementos.length]);
            if (i < tamanho - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
