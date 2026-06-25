# 📚 Documentação - Sistema de Biblioteca

## 📖 Guias Disponíveis

### 🚀 **Começar Aqui**
1. **[INSTALACAO.md](INSTALACAO.md)** - Como instalar Java e Maven
2. **[QUICK_START.md](QUICK_START.md)** - 3 passos para rodar a aplicação
3. **[README.md](README.md)** - Documentação completa do projeto

### 📋 **Como Usar**
- **[GUIA_TESTE.md](GUIA_TESTE.md)** - Guia de testes e exemplos de uso
- **[GUIA_PRATICO.md](GUIA_PRATICO.md)** - Casos de uso práticos

### 🏗️ **Estrutura Técnica**
- **[ESTRUTURAS_DADOS.md](ESTRUTURAS_DADOS.md)** - Documentação de estruturas customizadas
- **[INTEGRACAO.md](INTEGRACAO.md)** - Padrões e integrações

### 📑 **Referência**
- **[INDEX.md](INDEX.md)** - Índice geral do projeto
- **[SUMARIO_IMPLEMENTACAO.txt](SUMARIO_IMPLEMENTACAO.txt)** - Resumo da implementação

---

## ⚡ Quick Commands

```bash
# Compilar
mvn clean compile -DskipTests

# Rodar aplicação
mvn exec:java -Dexec.mainClass="br.edu.biblioteca.ui.MenuPrincipal" -DskipTests

# Rodar testes
mvn test

# Gerar JAR
mvn package -DskipTests

# Limpar
mvn clean
rm -rf data/
```

---

## 🎯 Recomendação de Leitura

**Usuário final?**
→ Leia: INSTALACAO → QUICK_START → GUIA_TESTE

**Desenvolvedor?**
→ Leia: README → ESTRUTURAS_DADOS → INTEGRACAO

**Avaliador/Professor?**
→ Leia: README → SUMARIO_IMPLEMENTACAO → ESTRUTURAS_DADOS

---

## 📁 Localização dos Arquivos

```
sistemadebiblioteca/
├── src/main/java/br/edu/biblioteca/     # Código fonte
├── src/test/java/br/edu/biblioteca/     # Testes unitários
├── data/                                 # Dados persistidos (CSV)
├── target/                               # Build output
│
├── README.md                             # Principal
├── QUICK_START.md                        # Iniciante
├── INSTALACAO.md                         # Dependências
├── GUIA_TESTE.md                         # Testes e exemplos
├── GUIA_PRATICO.md                       # Casos de uso
│
├── ESTRUTURAS_DADOS.md                   # Técnico
├── INTEGRACAO.md                         # Técnico
├── INDEX.md                              # Índice
├── SUMARIO_IMPLEMENTACAO.txt             # Resumo
│
├── pom.xml                               # Configuração Maven
├── teste_biblioteca.sh                   # Script de teste
└── DOCUMENTACAO.md                       # Este arquivo
```

---

## 🆘 Precisa de Ajuda?

1. **Não consegue instalar?** → Veja [INSTALACAO.md](INSTALACAO.md)
2. **Não consegue rodar?** → Veja [QUICK_START.md](QUICK_START.md)
3. **Não sabe como usar?** → Veja [GUIA_TESTE.md](GUIA_TESTE.md)
4. **Quer entender a arquitetura?** → Veja [README.md](README.md) e [ESTRUTURAS_DADOS.md](ESTRUTURAS_DADOS.md)
5. **Algo deu errado?** → Veja seção "Troubleshooting" em [README.md](README.md)

---

## ✅ Checklist de Configuração

- [ ] Java 11+ instalado
- [ ] Maven 3.8+ instalado
- [ ] Projeto compilado sem erros
- [ ] Aplicação roda sem crashes
- [ ] Dados são salvos em `data/`
- [ ] Testes passam

---

**Última atualização:** 2026-06-25
