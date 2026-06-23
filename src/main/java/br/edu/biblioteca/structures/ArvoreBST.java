package br.edu.biblioteca.structures;

import java.util.Iterator;

/**
 * Implementação de uma Árvore Binária de Busca (BST) genérica.
 * Útil para indexar livros por ISBN ou título com busca eficiente.
 *
 * @param <K> Tipo da chave (deve ser comparável)
 * @param <V> Tipo do valor
 */
public class ArvoreBST<K extends Comparable<K>, V> {
    private No raiz;
    private int tamanho;

    public ArvoreBST() {
        this.raiz = null;
        this.tamanho = 0;
    }

    /**
     * Insere uma chave-valor na árvore
     *
     * @param chave Chave a inserir
     * @param valor Valor associado
     */
    public void put(K chave, V valor) {
        if (chave == null) {
            throw new IllegalArgumentException("Chave não pode ser nula");
        }

        if (raiz == null) {
            raiz = new No(chave, valor);
            tamanho = 1;
        } else {
            if (put(raiz, chave, valor)) {
                tamanho++;
            }
        }
    }

    private boolean put(No no, K chave, V valor) {
        int cmp = chave.compareTo(no.chave);

        if (cmp < 0) {
            if (no.esquerda == null) {
                no.esquerda = new No(chave, valor);
                return true;
            } else {
                return put(no.esquerda, chave, valor);
            }
        } else if (cmp > 0) {
            if (no.direita == null) {
                no.direita = new No(chave, valor);
                return true;
            } else {
                return put(no.direita, chave, valor);
            }
        } else {
            no.valor = valor; // Atualiza valor se chave já existe
            return false;
        }
    }

    /**
     * Obtém o valor associado a uma chave
     *
     * @param chave Chave a procurar
     * @return Valor associado ou null se não encontrado
     */
    public V get(K chave) {
        if (chave == null) {
            return null;
        }
        No no = get(raiz, chave);
        return no != null ? no.valor : null;
    }

    private No get(No no, K chave) {
        if (no == null) {
            return null;
        }

        int cmp = chave.compareTo(no.chave);

        if (cmp < 0) {
            return get(no.esquerda, chave);
        } else if (cmp > 0) {
            return get(no.direita, chave);
        } else {
            return no;
        }
    }

    /**
     * Remove uma chave da árvore
     *
     * @param chave Chave a remover
     * @return Valor associado à chave removida ou null
     */
    public V remove(K chave) {
        if (chave == null) {
            return null;
        }

        No removido = get(raiz, chave);
        if (removido == null) {
            return null;
        }

        V valor = removido.valor;
        raiz = remove(raiz, chave);
        tamanho--;
        return valor;
    }

    private No remove(No no, K chave) {
        if (no == null) {
            return null;
        }

        int cmp = chave.compareTo(no.chave);

        if (cmp < 0) {
            no.esquerda = remove(no.esquerda, chave);
        } else if (cmp > 0) {
            no.direita = remove(no.direita, chave);
        } else {
            // Encontrou o nó a remover

            if (no.esquerda == null) {
                return no.direita;
            }
            if (no.direita == null) {
                return no.esquerda;
            }

            // Tem dois filhos - encontra o sucessor (menor da subárvore direita)
            No noAuxiliar = no;
            no = minimo(noAuxiliar.direita);
            no.direita = removerMinimo(noAuxiliar.direita);
            no.esquerda = noAuxiliar.esquerda;
        }

        return no;
    }

    private No minimo(No no) {
        if (no.esquerda == null) {
            return no;
        }
        return minimo(no.esquerda);
    }

    private No removerMinimo(No no) {
        if (no.esquerda == null) {
            return no.direita;
        }
        no.esquerda = removerMinimo(no.esquerda);
        return no;
    }

    /**
     * Verifica se a árvore contém uma chave
     *
     * @param chave Chave a procurar
     * @return true se contém
     */
    public boolean containsKey(K chave) {
        return get(chave) != null;
    }

    /**
     * Retorna o número de pares chave-valor
     *
     * @return Tamanho da árvore
     */
    public int size() {
        return tamanho;
    }

    /**
     * Verifica se a árvore está vazia
     *
     * @return true se vazia
     */
    public boolean isEmpty() {
        return tamanho == 0;
    }

    /**
     * Retorna um iterador sobre as chaves em ordem (in-order)
     *
     * @return Iterator sobre as chaves
     */
    public Iterator<K> inOrder() {
        return new InOrderIterator(raiz);
    }

    /**
     * Retorna um iterador sobre os valores em ordem (in-order)
     *
     * @return Iterator sobre os valores
     */
    public Iterator<V> values() {
        return new ValuesIterator(raiz);
    }

    private class InOrderIterator implements Iterator<K> {
        private Pilha<No> pilha = new Pilha<>();

        InOrderIterator(No no) {
            adicionarEsquerda(no);
        }

        private void adicionarEsquerda(No no) {
            while (no != null) {
                pilha.push(no);
                no = no.esquerda;
            }
        }

        @Override
        public boolean hasNext() {
            return !pilha.isEmpty();
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            No no = pilha.pop();
            adicionarEsquerda(no.direita);
            return no.chave;
        }
    }

    private class ValuesIterator implements Iterator<V> {
        private Pilha<No> pilha = new Pilha<>();

        ValuesIterator(No no) {
            adicionarEsquerda(no);
        }

        private void adicionarEsquerda(No no) {
            while (no != null) {
                pilha.push(no);
                no = no.esquerda;
            }
        }

        @Override
        public boolean hasNext() {
            return !pilha.isEmpty();
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            No no = pilha.pop();
            adicionarEsquerda(no.direita);
            return no.valor;
        }
    }

    private class No {
        K chave;
        V valor;
        No esquerda;
        No direita;

        No(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }
}
