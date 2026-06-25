# 🛠️ GUIA DE INSTALAÇÃO

## 📦 Instalação de Dependências

### Linux (Debian/Ubuntu)

```bash
# Atualizar pacotes
sudo apt-get update

# Instalar Java 17
sudo apt-get install openjdk-17-jdk

# Instalar Maven
sudo apt-get install maven

# Verificar instalação
java -version
javac -version
mvn -version
```

### macOS (Homebrew)

```bash
# Instalar Java 17
brew install openjdk@17
export PATH="/usr/local/opt/openjdk@17/bin:$PATH"

# Instalar Maven
brew install maven

# Verificar
java -version
mvn -version
```

### Windows (Git Bash + Chocolatey)

```bash
# Instalar Chocolatey (como Admin)
# https://chocolatey.org/install

# Instalar Java
choco install openjdk17

# Instalar Maven
choco install maven

# Verificar
java -version
mvn -version
```

---

## ✅ Verificação

Execute no terminal:

```bash
java -version
javac -version
mvn -version
```

Esperado:
- Java: `openjdk version "17.0.x"` (ou superior)
- Maven: `Apache Maven 3.8.x`

---

## 🚀 Próximo Passo

Após instalar, siga o [QUICK_START.md](QUICK_START.md)

```bash
cd ~/Área\ de\ trabalho/Projetos/sistemadebiblioteca
mvn clean compile -DskipTests
mvn exec:java -Dexec.mainClass="br.edu.biblioteca.ui.MenuPrincipal" -DskipTests
```
