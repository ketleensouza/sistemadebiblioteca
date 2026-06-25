# 🛠️ Guia Prático - Estruturas de Dados da Biblioteca

## 💡 Dicas de Uso

### Quando Usar Cada Estrutura

#### Vetor<T>
✅ **Use quando:**
- Precisa de acesso rápido por índice (O(1))
- Quer adicionar/remover elementos do final
- Precisa iterar sobre elementos sequencialmente

❌ **Evite quando:**
- Muitas remoções no meio da coleção
- Precisa de busca frequente

```java
// ✅ Bom
Vetor<Livro> livros = new Vetor<>();
livros.add(livro1);
Livro primeiro = livros.get(0);

// ❌ Ruim
livros.remove(livros.size() / 2); // Operação cara
```

#### Pilha<T>
✅ **Use quando:**
- Precisa rastrear histórico de operações
- Quer implementar Undo/Redo
- Precisa de LIFO (Last In First Out)

```java
// ✅ Undo/Redo
Pilha<String> desfazer = new Pilha<>();
Pilha<String> refazer = new Pilha<>();

desfazer.push("operacao1");
String op = desfazer.pop(); // Undo
refazer.push(op); // Para possibilitar Redo
```

#### Fila<T>
✅ **Use quando:**
- Precisa processar na ordem de chegada (FIFO)
- Implementar fila de espera
- Processamento assíncrono ordenado

```java
// ✅ Fila de reservas
Fila<Reserva> filaEspera = new Fila<>();

// Reservas entram por ordem
for (Reserva r : listaReservas) {
    filaEspera.enqueue(r);
}

// Processam em ordem FIFO
while (!filaEspera.isEmpty()) {
    Reserva pronta = filaEspera.dequeue();
    processarReserva(pronta);
}
```

#### Matriz<T>
✅ **Use quando:**
- Dados tabulares (2D)
- Estatísticas por período e categoria
- Precisa de transposição

```java
// ✅ Estatísticas
Matriz<Integer> stats = new Matriz<>(12, 10); // 12 meses, 10 categorias

stats.set(0, 2, 15); // Janeiro, Categoria 2: 15 empréstimos
int valor = stats.get(0, 2);

// Análise
for (int mes = 0; mes < 12; mes++) {
    Vetor<Integer> mesData = stats.getLinha(mes);
    int totalMes = 0;
    for (int cat = 0; cat < mesData.size(); cat++) {
        totalMes += mesData.get(cat);
    }
    System.out.println("Mês " + mes + ": " + totalMes);
}
```

#### ArvoreBST<K, V>
✅ **Use quando:**
- Precisa buscar rapidamente (O(log n))
- Valores têm ordem natural
- Quer iteração ordenada

❌ **Evite quando:**
- Pior caso O(n) não é aceitável
- Dados muito desbalanceados

```java
// ✅ Índice de livros
ArvoreBST<String, Livro> indice = new ArvoreBST<>();

indice.put("978-0-13-468599-1", livroCleanCode);
Livro encontrado = indice.get("978-0-13-468599-1"); // O(log n)

// Iteração ordenada
Iterator<String> iter = indice.inOrder();
while (iter.hasNext()) {
    System.out.println("ISBN: " + iter.next());
}
```

#### Grafo<T>
✅ **Use quando:**
- Relacionamentos complexos
- Recomendações baseadas em conexões
- Análise de redes

```java
// ✅ Recomendações
Grafo<String> recomendacoes = new Grafo<>();

// Livros relacionados
recomendacoes.adicionarAresta("Clean Code", "Design Patterns");
recomendacoes.adicionarAresta("Design Patterns", "Refactoring");

// Encontrar sugestões
Vetor<String> sugestoes = recomendacoes.buscaProfundidade("Clean Code");
```

---

## 🐛 Troubleshooting

### Problema: IndexOutOfBoundsException em Vetor
**Causa:** Tentando acessar índice fora do intervalo
```java
Vetor<String> v = new Vetor<>();
v.add("a");
String s = v.get(1); // ❌ IndexOutOfBoundsException
```

**Solução:** Verificar tamanho antes
```java
if (indice >= 0 && indice < v.size()) {
    String s = v.get(indice);
}
```

### Problema: IllegalStateException em Pilha/Fila
**Causa:** Tentar pop/dequeue em estrutura vazia
```java
Pilha<Integer> p = new Pilha<>();
int x = p.pop(); // ❌ IllegalStateException
```

**Solução:** Verificar antes
```java
if (!p.isEmpty()) {
    int x = p.pop();
}
```

### Problema: Arvorce desbalanceada em ArvoreBST
**Causa:** Inserções em ordem causam pior caso O(n)
```java
ArvoreBST<Integer, String> tree = new ArvoreBST<>();
for (int i = 0; i < 1000; i++) {
    tree.put(i, "valor" + i); // ❌ Árvore linear, O(n)
}
```

**Solução:** Considerar dados aleatórios ou usar AVL/RB-Tree
```java
// Inserir em ordem aleatória
List<Integer> chaves = Arrays.asList(...);
Collections.shuffle(chaves);
for (int chave : chaves) {
    tree.put(chave, "valor" + chave); // ✅ Melhor balanceamento
}
```

### Problema: Vazamento de Memória em Grafo
**Causa:** Não remover vértices desconectados
```java
Grafo<String> g = new Grafo<>();
g.adicionarVertice("a");
g.adicionarVertice("b");
// ... trabalhar ...
// Vértices nunca removidos
```

**Solução:** Limpar quando não mais necessário
```java
g.removerVertice("a");
g.removerVertice("b");
// Ou
g = new Grafo<>(); // Criar novo
```

---

## ⚡ Performance

### Otimizações Práticas

#### 1. Vetor - Evitar Realocações
```java
// ❌ Lento - muitas realocações
Vetor<Integer> v = new Vetor<>();
for (int i = 0; i < 10000; i++) {
    v.add(i); // Realoca quando necessário
}

// ✅ Rápido - evita realocações
Vetor<Integer> v = new Vetor<>(10000); // Pré-aloca
for (int i = 0; i < 10000; i++) {
    v.add(i);
}
```

#### 2. ArvoreBST - Manter Balanceado
```java
// Balanceamento automático (não implementado)
// Para milhões de operações, considere AVL ou Red-Black Tree
```

#### 3. Grafo - Cache de Adjacentes
```java
// ❌ Recalcula a cada vez
Vetor<String> adj1 = grafo.obterAdjacentes("A");
Vetor<String> adj2 = grafo.obterAdjacentes("A");

// ✅ Cache resultado
Vetor<String> adj = grafo.obterAdjacentes("A");
// Reusar adj
```

#### 4. Fila - Evitar Enfileiramento Excessivo
```java
// ❌ Fila cresce infinitamente
while (true) {
    fila.enqueue(novaReserva());
    if (!fila.isEmpty()) {
        fila.dequeue();
    }
}

// ✅ Limpar periodicamente
if (fila.size() > LIMITE_MAXIMO) {
    fila.clear();
    logger.warn("Fila limite atingido!");
}
```

---

## 📋 Checklist de Implementação

### Para Adicionar Novo Serviço com Estrutura

- [ ] Criar classe Service em `br.edu.biblioteca.service`
- [ ] Injetar estrutura apropriada no construtor
- [ ] Implementar métodos de negócio
- [ ] Adicionar logging de operações
- [ ] Criar testes unitários
- [ ] Documentar casos de uso
- [ ] Adicionar tratamento de exceções
- [ ] Validar entrada de dados

### Exemplo Pronto para Copiar
```java
package br.edu.biblioteca.service;

import br.edu.biblioteca.structures.Vetor;

/**
 * Serviço novo com Vetor.
 */
public class NovoServico {
    private Vetor<String> dados;

    public NovoServico() {
        this.dados = new Vetor<>();
    }

    public void adicionar(String valor) {
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        dados.add(valor);
    }

    public String obter(int indice) {
        if (indice < 0 || indice >= dados.size()) {
            throw new IndexOutOfBoundsException("Índice: " + indice);
        }
        return dados.get(indice);
    }

    public int getTamanho() {
        return dados.size();
    }
}
```

---

## 🔗 Integração com JPA

### Salvando Índices em Banco

```java
@Entity
public class IndiceCache {
    @Id
    private String chave;
    
    @Column(columnDefinition = "LONGTEXT")
    private String indice; // JSON serializado
    
    @Column
    private LocalDateTime ultimaAtualizacao;
}

// Service
public class CacheService {
    public void salvarIndice(ArvoreBST<String, Livro> arvore) {
        String json = serializarArvore(arvore);
        IndiceCache cache = new IndiceCache();
        cache.setChave("livros");
        cache.setIndice(json);
        cache.setUltimaAtualizacao(LocalDateTime.now());
        repository.save(cache);
    }
    
    public ArvoreBST<String, Livro> carregarIndice() {
        IndiceCache cache = repository.findById("livros").orElse(null);
        if (cache != null) {
            return desserializarArvore(cache.getIndice());
        }
        return new ArvoreBST<>();
    }
}
```

---

## 📊 Monitoramento

### Logging de Operações
```java
logger.info("Adicionando livro ao índice: {}", isbn);
logger.debug("Tamanho da árvore: {}", indice.size());
logger.warn("Fila de reservas acima de {} items", limite);
logger.error("Erro ao processar reserva", exception);
```

### Métricas
```java
public class MetricasService {
    private int operacoesDesfazer = 0;
    private int operacoesRefazer = 0;
    private int buscasISBN = 0;
    
    public void relatorioMetricas() {
        System.out.println("Operações Desfazer: " + operacoesDesfazer);
        System.out.println("Operações Refazer: " + operacoesRefazer);
        System.out.println("Buscas ISBN: " + buscasISBN);
    }
}
```

---

## ✨ Melhores Práticas

1. **Sempre validar entrada** antes de adicionar à estrutura
2. **Usar genéricos** para type-safety
3. **Implementar `Iterable`** para foreach loops
4. **Tratar `IndexOutOfBoundsException`** apropriadamente
5. **Documentar complexidade** das operações
6. **Usar constantes** para limites (Ex: MAX_TAMANHO)
7. **Testes primeiro** (TDD) ao implementar
8. **Cache quando apropriado** para operações caras

---

## 📞 Suporte

### Problemas Comuns

| Problema | Solução |
|----------|---------|
| Lento | Verificar complexidade, usar estrutura adequada |
| Memória | Verificar realocações desnecessárias, limpar estrutura |
| Erro de Índice | Validar limites antes de acesso |
| Estrutura Vazia | Usar `isEmpty()` antes de operações |
| Não Encontrado | Retornar `null` ou `Optional`, não lançar exceção |

---

**Última Atualização:** 2026-06-22
