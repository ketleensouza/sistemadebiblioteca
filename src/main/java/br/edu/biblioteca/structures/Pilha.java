package br.edu.biblioteca.structures;

import java.util.Iterator;

/**
 * Implementação de uma pilha (stack) genérica - LIFO (Last In First Out).
 * Útil para operações de Undo/Redo.
 *
 * @param <T> Tipo dos elementos armazenados
 */
public class Pilha<T> implements Iterable<T> {
    private static final int CAPACIDADE_INICIAL = 10;

    private Object[] elementos;
    private int topo;

    public Pilha() {
        this.elementos = new Object[CAPACIDADE_INICIAL];
        this.topo = -1;
    }

    /**
     * Adiciona um elemento no topo da pilha
     *
     * @param elemento Elemento a adicionar
     */
    public void push(T elemento) {
        if (topo + 1 == elementos.length) {
            redimensionar(elementos.length * 2);
        }
        elementos[++topo] = elemento;
    }

    /**
     * Remove e retorna o elemento no topo da pilha
     *
     * @return Elemento no topo
     * @throws IllegalStateException Se a pilha estiver vazia
     */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Pilha vazia");
        }
        Object elemento = elementos[topo];
        elementos[topo--] = null;

        // Redimensiona se necessário
        if (topo >= 0 && topo < elementos.length / 4) {
            redimensionar(elementos.length / 2);
        }

        return (T) elemento;
    }

    /**
     * Retorna o elemento no topo sem remover
     *
     * @return Elemento no topo
     * @throws IllegalStateException Se a pilha estiver vazia
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Pilha vazia");
        }
        return (T) elementos[topo];
    }

    /**
     * Verifica se a pilha está vazia
     *
     * @return true se vazia
     */
    public boolean isEmpty() {
        return topo == -1;
    }

    /**
     * Retorna o número de elementos na pilha
     *
     * @return Tamanho da pilha
     */
    public int size() {
        return topo + 1;
    }

    /**
     * Remove todos os elementos da pilha
     */
    public void clear() {
        for (int i = 0; i <= topo; i++) {
            elementos[i] = null;
        }
        topo = -1;
    }

    private void redimensionar(int novaCapacidade) {
        Object[] novoArray = new Object[novaCapacidade];
        System.arraycopy(elementos, 0, novoArray, 0, topo + 1);
        elementos = novoArray;
    }

    @Override
    public Iterator<T> iterator() {
        return new PilhaIterator();
    }

    private class PilhaIterator implements Iterator<T> {
        private int indiceAtual = topo;

        @Override
        public boolean hasNext() {
            return indiceAtual >= 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return (T) elementos[indiceAtual--];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = topo; i >= 0; i--) {
            sb.append(elementos[i]);
            if (i > 0) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
