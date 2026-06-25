# Estruturas de Dados - Sistema de Biblioteca

## Visão Geral

Este documento descreve as estruturas de dados implementadas para o sistema de biblioteca em Java. Todas as estruturas estão localizadas no pacote `br.edu.biblioteca.structures`.

## Estruturas Implementadas

### 1. **Vetor<T>** - Array Dinâmico
**Localização:** `br.edu.biblioteca.structures.Vetor`

**Descrição:** Implementação de um vetor dinâmico genérico com redimensionamento automático.

**Operações principais:**
- `add(T elemento)` - Adiciona elemento ao final (O(1) amortizado)
- `add(int indice, T elemento)` - Insere em posição específica (O(n))
- `get(int indice)` - Retorna elemento (O(1))
- `set(int indice, T elemento)` - Modifica elemento (O(1))
- `remove(int indice)` - Remove e retorna elemento (O(n))
- `remove(Object elemento)` - Remove primeira ocorrência (O(n))
- `contains(Object elemento)` - Verifica existência (O(n))
- `size()` - Retorna tamanho (O(1))
- `isEmpty()` - Verifica se vazio (O(1))

**Características:**
- Fator de crescimento: 2x quando cheio
- Redimensiona para baixo quando 1/4 cheio
- Implementa `Iterable<T>`

**Use quando:** Precisa de acesso rápido por índice e operações de adição/remoção sequencial.

---

### 2. **Pilha<T>** - Stack (LIFO)
**Localização:** `br.edu.biblioteca.structures.Pilha`

**Descrição:** Implementação de uma pilha genérica - Last In First Out.

**Operações principais:**
- `push(T elemento)` - Adiciona ao topo (O(1) amortizado)
- `pop()` - Remove e retorna topo (O(1))
- `peek()` - Visualiza topo sem remover (O(1))
- `isEmpty()` - Verifica se vazia (O(1))
- `size()` - Retorna tamanho (O(1))

**Uso no Sistema:**
- **Undo/Redo:** Rastrear operações para desfazer
- **Histórico de Empréstimos:** `IndiceEmprestimoService`
- **Navegação:** Histórico de páginas visitadas

**Exemplo:**
```java
Pilha<Emprestimo> historico = new Pilha<>();
historico.push(emprestimo1);
historico.push(emprestimo2);
Emprestimo ultimoDesfeito = historico.pop(); // Undo
```

---

### 3. **Fila<T>** - Queue (FIFO)
**Localização:** `br.edu.biblioteca.structures.Fila`

**Descrição:** Implementação de uma fila genérica - First In First Out.

**Operações principais:**
- `enqueue(T elemento)` - Adiciona ao final (O(1) amortizado)
- `dequeue()` - Remove e retorna início (O(1))
- `peek()` - Visualiza início sem remover (O(1))
- `isEmpty()` - Verifica se vazia (O(1))
- `size()` - Retorna tamanho (O(1))

**Características:**
- Implementa fila circular para eficiência
- Redimensiona dinamicamente

**Uso no Sistema:**
- **Reservas:** `GerenciadorReservasService` - filas de espera
- **Lista de Espera:** Processar reservas em ordem
- **Processamento em Lote:** Multas, notificações

**Exemplo:**
```java
Fila<Reserva> filaReservas = new Fila<>();
filaReservas.enqueue(reserva1);
filaReservas.enqueue(reserva2);
Reserva pronta = filaReservas.dequeue(); // Processa primeira
```

---

### 4. **Matriz<T>** - Array 2D
**Localização:** `br.edu.biblioteca.structures.Matriz`

**Descrição:** Implementação de uma matriz genérica 2D.

**Operações principais:**
- `get(int linha, int coluna)` - Retorna elemento (O(1))
- `set(int linha, int coluna, T elemento)` - Define elemento (O(1))
- `getLinha(int linha)` - Retorna linha como Vetor (O(n))
- `getColuna(int coluna)` - Retorna coluna como Vetor (O(n))
- `transpor()` - Retorna matriz transposta (O(n*m))

**Uso no Sistema:**
- **Estatísticas:** `EstatisticasService`
  - Empréstimos por Mês x Categoria (12 meses x 10 categorias)
  - Análise de tendências
- **Relatórios:** Visualização tabular de dados

**Exemplo:**
```java
Matriz<Integer> emprestimosPorMes = new Matriz<>(12, 10);
emprestimosPorMes.set(0, 2, 15); // Janeiro, Categoria 2: 15 empréstimos
```

---

### 5. **ArvoreBST<K, V>** - Binary Search Tree
**Localização:** `br.edu.biblioteca.structures.ArvoreBST`

**Descrição:** Árvore Binária de Busca genérica com chaves ordenadas.

**Operações principais:**
- `put(K chave, V valor)` - Insere ou atualiza (O(log n) médio, O(n) pior)
- `get(K chave)` - Busca por chave (O(log n) médio, O(n) pior)
- `remove(K chave)` - Remove por chave (O(log n) médio, O(n) pior)
- `containsKey(K chave)` - Verifica existência (O(log n) médio)
- `inOrder()` - Iterador em ordem (O(n))
- `values()` - Iterador sobre valores (O(n))

**Características:**
- Mantém chaves ordenadas
- Suporta iteração in-order
- Remoção com tratamento de 2 filhos

**Uso no Sistema:**
- **Índice de Livros:** `IndiceLivroService`
  - Busca rápida por ISBN
  - Busca por título normalizado
  - Índices para performance O(log n)

**Exemplo:**
```java
ArvoreBST<String, Livro> indice = new ArvoreBST<>();
indice.put("978-0-13-468599-1", livroCleanCode);
Livro encontrado = indice.get("978-0-13-468599-1");
```

---

### 6. **Grafo<T>** - Graph
**Localização:** `br.edu.biblioteca.structures.Grafo`

**Descrição:** Implementação de um grafo direcionado com lista de adjacência.

**Operações principais:**
- `adicionarVertice(T vertice)` - Adiciona vértice (O(log n))
- `adicionarAresta(T origem, T destino)` - Adiciona aresta (O(log n))
- `removerAresta(T origem, T destino)` - Remove aresta (O(n))
- `removerVertice(T vertice)` - Remove vértice (O(n²))
- `obterAdjacentes(T vertice)` - Lista de vértices conectados (O(1))
- `grau(T vertice)` - Número de arestas (O(1))
- `temAresta(T origem, T destino)` - Verifica conexão (O(n))
- `buscaProfundidade(T inicio)` - DFS (O(V + E))
- `buscaLargura(T inicio)` - BFS (O(V + E))

**Características:**
- Direcionado
- Lista de adjacência com Vetor
- Busca DFS e BFS

**Uso no Sistema:**
- **Recomendações:** `SistemaRecomendacaoService`
  - Grafo de relacionamentos entre livros
  - BFS para livros similares
  - DFS para recomendações profundas
- **Análise de Tendências:** Padrões de leitura
- **Redes Sociais:** Conexões entre categorias/autores

**Exemplo:**
```java
Grafo<String> recomendacoes = new Grafo<>();
recomendacoes.adicionarAresta("Clean Code", "Design Patterns");
Vetor<String> relacionados = recomendacoes.buscaProfundidade("Clean Code");
```

---

## Serviços Implementados

### 1. **IndiceEmprestimoService**
- Gerencia histórico de empréstimos com Undo/Redo
- Usa `Pilha<Emprestimo>` para operações
- Métodos: `registrarEmprestimo()`, `desfazer()`, `refazer()`

### 2. **IndiceLivroService**
- Indexa livros por ISBN e título
- Usa `ArvoreBST<String, Livro>` para buscas O(log n)
- Métodos: `buscarPorISBN()`, `buscarPorTitulo()`, `adicionarLivro()`

### 3. **GerenciadorReservasService**
- Gerencia filas de espera de reservas
- Usa `Fila<Reserva>` com ordem FIFO
- Métodos: `adicionarReserva()`, `processarProximaReserva()`, `verProximaReserva()`

### 4. **SistemaRecomendacaoService**
- Sistema de recomendação baseado em grafo
- Usa `Grafo<String>` para relacionamentos
- Métodos: `adicionarRecomendacao()`, `obterRecomendacoes()`, `obterRecomendacoesDistantes()`

### 5. **EstatisticasService**
- Relatórios e análises de empréstimos
- Usa `Matriz<Integer>` (12 meses x 10 categorias)
- Métodos: `registrarEmprestimo()`, `getTotalMes()`, `gerarRelatorio()`

---

## Testes Unitários

Testes disponíveis em `src/test/java/br/edu/biblioteca/structures/`:

- `VetorTest.java` - Testes para Vetor
- `PilhaTest.java` - Testes para Pilha
- `FilaTest.java` - Testes para Fila
- `ArvoreBSTTest.java` - Testes para ArvoreBST

Executar testes:
```bash
mvn test
```

---

## Exemplo de Uso Completo

Ver arquivo `ExemploUsoDadosEstruturados.java` em `br.edu.biblioteca.util`

```bash
java br.edu.biblioteca.util.ExemploUsoDadosEstruturados
```

---

## Complexidade de Operações

| Estrutura | Busca | Inserção | Remoção | Acesso |
|-----------|-------|----------|---------|--------|
| Vetor | O(n) | O(n) | O(n) | O(1) |
| Pilha | O(n) | O(1)* | O(1)* | - |
| Fila | O(n) | O(1)* | O(1)* | - |
| Matriz | O(1) | O(1) | O(1) | O(1) |
| ArvoreBST | O(log n)** | O(log n)** | O(log n)** | - |
| Grafo | O(V+E) | O(log n) | O(n²) | O(1) |

*Amortizado | **Médio (pior: O(n))

---

## Boas Práticas

1. **Use Vetor** quando precisar de acesso rápido por índice
2. **Use Pilha** para histórico e operações reversíveis
3. **Use Fila** para processamento ordenado (FIFO)
4. **Use Matriz** para dados tabulares e estatísticas
5. **Use ArvoreBST** para índices com busca eficiente
6. **Use Grafo** para relacionamentos complexos

---

## Próximos Passos

- [ ] Integração com banco de dados
- [ ] Persistência de índices
- [ ] Otimizações de performance
- [ ] Sincronização para ambiente multi-thread
- [ ] Serialização das estruturas
