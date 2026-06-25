# Sistema de Biblioteca

Sistema de gestão de biblioteca em Java com interface CLI, persistência em CSV e estruturas de dados customizadas.

Alunos: Ketleen de Souza Santos, Arthur Xavier dos Santos, Kenedy Anderson Souza de Castro.

---

## Quick Start

### 1. Instalar Dependências
```bash
sudo apt-get install openjdk-17-jdk maven
```

### 2. Compilar
```bash
cd ~/Área\ de\ trabalho/Projetos/sistemadebiblioteca
mvn clean compile -DskipTests
```

### 3. Rodar
```bash
mvn exec:java -Dexec.mainClass="br.edu.biblioteca.ui.MenuPrincipal" -DskipTests
```

---

## Menu Principal

```
1 - Catálogo de Livros          (Listar, buscar, cadastrar, atualizar, remover)
2 - Gerenciar Usuários          (CRUD de usuários)
3 - Empréstimos                 (Novo empréstimo, devolução, atrasados)
4 - Reservas                    (Fazer, cancelar, listar reservas)
5 - Relatórios                  (Estatísticas e relatórios gerais)
0 - Sair
```

---

## Estrutura

```
src/main/java/br/edu/biblioteca/
├── action/       → Padrão Command
├── model/        → Livro, Usuario, Emprestimo, Reserva, Exemplar
├── repository/   → FileStorage, LivroRepository, UsuarioRepository...
├── service/      → Lógica de negócio
├── structures/   → Vetor, Pilha, Fila, ArvoreBST, Grafo
└── ui/           → MenuPrincipal, TelaCatalogo, TelaUsuarios...
```

---

## Dados

Salvos em `data/*.csv`:
- `livros.csv` - Catálogo
- `usuarios.csv` - Usuários
- `exemplares.csv` - Cópias
- `emprestimos.csv` - Empréstimos
- `reservas.csv` - Reservas

---

## Testes

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=VetorTest
mvn test -Dtest=PilhaTest
```

---

## Troubleshooting

**Maven não encontrado:**
```bash
sudo apt-get install maven
```

**Limparcache:**
```bash
mvn clean
rm -rf data/
```


