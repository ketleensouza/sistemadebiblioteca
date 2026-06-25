# 🚀 Quick Start - Sistema de Biblioteca

## ⚡ Iniciar em 3 passos

### 1. Compilar
```bash
cd ~/Área\ de\ trabalho/Projetos/sistemadebiblioteca
mvn clean compile -DskipTests
```

### 2. Rodar
```bash
mvn exec:java -Dexec.mainClass="br.edu.biblioteca.ui.MenuPrincipal" -DskipTests
```

### 3. Usar
```
1 - Catálogo de Livros
  └─ 3 - Cadastrar Novo Livro
     ├─ ISBN: 978-8500054841
     ├─ Título: O Alienista
     ├─ Ano: 1882
     └─ Palavras-chave: clássico,brasileiro
```

---

## 📋 Fluxo Completo de Teste

### Menu 1: Catálogo de Livros
```
1 → Listar Livros (ver lista vazia)
3 → Cadastrar um livro
1 → Listar novamente (deve aparecer o livro)
2 → Buscar por ISBN (do livro cadastrado)
4 → Atualizar dados
5 → Remover (opcional)
```

### Menu 2: Usuários
```
1 → Listar Usuários
4 → Cadastrar Usuário
3 → Buscar por Email (do usuário cadastrado)
```

### Menu 5: Relatórios
```
1 → Ver Relatório de Livros
2 → Ver Relatório de Usuários
6 → Ver Estatísticas Gerais
```

---

## 💾 Dados Salvos

Após usar, verifique:
```bash
ls data/
cat data/livros.csv
cat data/usuarios.csv
```

---

## 🧪 Rodar Testes Unitários

```bash
mvn test
```

---

## 🆘 Algo deu errado?

```bash
# Limpar tudo
mvn clean
rm -rf data/

# Recomeçar
mvn compile -DskipTests
mvn exec:java -Dexec.mainClass="br.edu.biblioteca.ui.MenuPrincipal" -DskipTests
```

---

Para mais detalhes, veja [README.md](README.md) ou [GUIA_TESTE.md](GUIA_TESTE.md)
