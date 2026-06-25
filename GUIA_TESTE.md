# 📚 GUIA DE TESTE - SISTEMA DE BIBLIOTECA

## 🚀 INICIAR APLICAÇÃO

### Opção 1: Executar com Maven (Recomendado)
```bash
cd /home/ketleen/Área\ de\ trabalho/Projetos/sistemadebiblioteca
mvn exec:java -Dexec.mainClass="br.edu.biblioteca.ui.MenuPrincipal" -DskipTests
```

### Opção 2: Gerar JAR e Executar
```bash
mvn clean package -DskipTests
java -jar target/sistema-biblioteca-1.0-SNAPSHOT.jar
```

### Opção 3: Script Automático
```bash
bash teste_biblioteca.sh
```

---

## 📝 TESTE 1: CADASTRAR LIVRO

**Caminho:** Menu Principal → 1 (Catálogo) → 3 (Cadastrar Novo Livro)

**Dados de Exemplo:**
```
ISBN: 978-8500054841
Título: O Alienista
Ano de publicação: 1882
Palavras-chave: clássico,brasileiro,ficção
```

**Esperado:** ✓ Livro cadastrado com sucesso!

---

## 📖 TESTE 2: LISTAR LIVROS

**Caminho:** Menu Principal → 1 (Catálogo) → 1 (Listar Livros)

**Esperado:** Exibe lista de livros cadastrados ou mensagem "Nenhum livro cadastrado"

---

## 🔍 TESTE 3: BUSCAR LIVRO

**Caminho:** Menu Principal → 1 (Catálogo) → 2 (Buscar por ISBN)

**Dados de Exemplo:**
```
ISBN: 978-8500054841
```

**Esperado:** Exibe dados do livro ou "Livro não encontrado"

---

## 👤 TESTE 4: CADASTRAR USUÁRIO

**Caminho:** Menu Principal → 2 (Usuários) → 4 (Cadastrar Novo Usuário)

**Dados de Exemplo:**
```
Nome: João Silva
Email: joao@example.com
Tipo: 1 (ALUNO)
```

**Esperado:** ✓ Usuário cadastrado com sucesso!

---

## 📊 TESTE 5: VER RELATÓRIOS

**Caminho:** Menu Principal → 5 (Relatórios)

**Opções Disponíveis:**
1. Relatório de Livros
2. Relatório de Usuários
3. Empréstimos Ativos
4. Empréstimos Atrasados
5. Reservas
6. Estatísticas Gerais

**Esperado:** Exibição de dados ou mensagens de "Nenhum registro"

---

## 💾 VERIFICAR DADOS SALVOS

Após testes, verifique os arquivos CSV:

```bash
ls -la ~/Área\ de\ trabalho/Projetos/sistemadebiblioteca/data/
cat ~/Área\ de\ trabalho/Projetos/sistemadebiblioteca/data/livros.csv
```

**Arquivos criados:**
- `livros.csv` - Catálogo de livros
- `usuarios.csv` - Usuários registrados
- `exemplares.csv` - Exemplares dos livros
- `emprestimos.csv` - Registros de empréstimos
- `reservas.csv` - Reservas de livros

---

## 🧪 RODAR TESTES UNITÁRIOS

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=VetorTest
mvn test -Dtest=PilhaTest
mvn test -Dtest=FilaTest
mvn test -Dtest=ArvoreBSTTest

# Teste de cobertura
mvn clean test jacoco:report
```

---

## 🐛 TROUBLESHOOTING

**Erro: "comando mvn não encontrado"**
```bash
sudo apt-get install maven
```

**Erro: "Java version mismatch"**
```bash
javac -version  # Deve ser 11+
java -version
```

**Limpar builds anteriores**
```bash
mvn clean
rm -rf data/*.csv
```

---

## 📌 CHECKLIST DE TESTES

- [ ] Compilação sem erros
- [ ] Cadastrar livro com sucesso
- [ ] Listar livros cadastrados
- [ ] Buscar livro por ISBN
- [ ] Atualizar livro
- [ ] Remover livro
- [ ] Cadastrar usuário
- [ ] Listar usuários
- [ ] Buscar usuário por email
- [ ] Ver relatórios
- [ ] Dados salvos em CSV

---

**Dúvidas?** Verifique `README.md` no projeto.
