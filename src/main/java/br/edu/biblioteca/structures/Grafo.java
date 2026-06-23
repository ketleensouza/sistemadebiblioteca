package br.edu.biblioteca.structures;

import java.util.Iterator;

/**
 * Implementação de um Grafo genérico com lista de adjacência.
 * Útil para recomendações de livros ou modelar relações entre categorias/autores.
 *
 * @param <T> Tipo dos vértices
 */
public class Grafo<T extends Comparable<T>> {
    private ArvoreBST<T, Vetor<T>> adjacencia;
    private int numVertices;
    private int numArestas;

    public Grafo() {
        this.adjacencia = new ArvoreBST<T, Vetor<T>>();
        this.numVertices = 0;
        this.numArestas = 0;
    }

    /**
     * Adiciona um vértice ao grafo
     *
     * @param vertice Vértice a adicionar
     */
    public void adicionarVertice(T vertice) {
        if (!adjacencia.containsKey(vertice)) {
            adjacencia.put(vertice, new Vetor<>());
            numVertices++;
        }
    }

    /**
     * Adiciona uma aresta do vértice origem para destino
     *
     * @param origem Vértice de origem
     * @param destino Vértice de destino
     */
    public void adicionarAresta(T origem, T destino) {
        if (origem == null || destino == null) {
            throw new IllegalArgumentException("Vértices não podem ser nulos");
        }

        adicionarVertice(origem);
        adicionarVertice(destino);

        Vetor<T> adjacentes = adjacencia.get(origem);
        if (!adjacentes.contains(destino)) {
            adjacentes.add(destino);
            numArestas++;
        }
    }

    /**
     * Remove uma aresta
     *
     * @param origem Vértice de origem
     * @param destino Vértice de destino
     * @return true se removida
     */
    public boolean removerAresta(T origem, T destino) {
        if (!adjacencia.containsKey(origem)) {
            return false;
        }

        Vetor<T> adjacentes = adjacencia.get(origem);
        if (adjacentes.remove(destino)) {
            numArestas--;
            return true;
        }
        return false;
    }

    /**
     * Verifica se existe aresta entre dois vértices
     *
     * @param origem Vértice de origem
     * @param destino Vértice de destino
     * @return true se existe aresta
     */
    public boolean temAresta(T origem, T destino) {
        if (!adjacencia.containsKey(origem)) {
            return false;
        }
        return adjacencia.get(origem).contains(destino);
    }

    /**
     * Retorna os vértices adjacentes de um vértice
     *
     * @param vertice Vértice
     * @return Vetor com vértices adjacentes
     */
    public Vetor<T> obterAdjacentes(T vertice) {
        if (!adjacencia.containsKey(vertice)) {
            return new Vetor<>();
        }
        return adjacencia.get(vertice);
    }

    /**
     * Retorna o grau de um vértice (número de arestas saindo dele)
     *
     * @param vertice Vértice
     * @return Grau do vértice
     */
    public int grau(T vertice) {
        if (!adjacencia.containsKey(vertice)) {
            return 0;
        }
        return adjacencia.get(vertice).size();
    }

    /**
     * Remove um vértice do grafo
     *
     * @param vertice Vértice a remover
     * @return true se removido
     */
    public boolean removerVertice(T vertice) {
        if (!adjacencia.containsKey(vertice)) {
            return false;
        }

        // Remove todas as arestas saindo deste vértice
        Vetor<T> adjacentes = adjacencia.get(vertice);
        numArestas -= adjacentes.size();

        // Remove todas as arestas chegando neste vértice
        Iterator<T> iter = adjacencia.inOrder();
        while (iter.hasNext()) {
            T v = iter.next();
            if (!v.equals(vertice)) {
                Vetor<T> adjV = adjacencia.get(v);
                if (adjV.remove(vertice)) {
                    numArestas--;
                }
            }
        }

        adjacencia.remove(vertice);
        numVertices--;
        return true;
    }

    /**
     * Retorna o número de vértices
     *
     * @return Número de vértices
     */
    public int numVertices() {
        return numVertices;
    }

    /**
     * Retorna o número de arestas
     *
     * @return Número de arestas
     */
    public int numArestas() {
        return numArestas;
    }

    /**
     * Verifica se o grafo contém um vértice
     *
     * @param vertice Vértice
     * @return true se contém
     */
    public boolean contemVertice(T vertice) {
        return adjacencia.containsKey(vertice);
    }

    /**
     * Realiza busca em profundidade (DFS) a partir de um vértice
     *
     * @param inicio Vértice de início
     * @return Vetor com ordem de visitação
     */
    public Vetor<T> buscaProfundidade(T inicio) {
        if (!contemVertice(inicio)) {
            return new Vetor<>();
        }

        Vetor<T> visitados = new Vetor<>();
        Pilha<T> pilha = new Pilha<>();
        ArvoreBST<T, Boolean> marcados = new ArvoreBST<>();

        pilha.push(inicio);

        while (!pilha.isEmpty()) {
            T vertice = pilha.pop();

            if (marcados.get(vertice) == null) {
                marcados.put(vertice, true);
                visitados.add(vertice);

                Vetor<T> adjacentes = obterAdjacentes(vertice);
                for (int i = adjacentes.size() - 1; i >= 0; i--) {
                    T adj = adjacentes.get(i);
                    if (marcados.get(adj) == null) {
                        pilha.push(adj);
                    }
                }
            }
        }

        return visitados;
    }

    /**
     * Realiza busca em largura (BFS) a partir de um vértice
     *
     * @param inicio Vértice de início
     * @return Vetor com ordem de visitação
     */
    public Vetor<T> buscaLargura(T inicio) {
        if (!contemVertice(inicio)) {
            return new Vetor<>();
        }

        Vetor<T> visitados = new Vetor<>();
        Fila<T> fila = new Fila<>();
        ArvoreBST<T, Boolean> marcados = new ArvoreBST<>();

        fila.enqueue(inicio);
        marcados.put(inicio, true);

        while (!fila.isEmpty()) {
            T vertice = fila.dequeue();
            visitados.add(vertice);

            Vetor<T> adjacentes = obterAdjacentes(vertice);
            for (int i = 0; i < adjacentes.size(); i++) {
                T adj = adjacentes.get(i);
                if (marcados.get(adj) == null) {
                    marcados.put(adj, true);
                    fila.enqueue(adj);
                }
            }
        }

        return visitados;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Grafo{\n");
        Iterator<T> iter = adjacencia.inOrder();
        while (iter.hasNext()) {
            T vertice = iter.next();
            sb.append("  ").append(vertice).append(" -> ")
                    .append(obterAdjacentes(vertice)).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
