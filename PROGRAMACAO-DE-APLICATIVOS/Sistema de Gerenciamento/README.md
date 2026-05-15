# 🐾 Clínica Veterinária — Sistema de Gerenciamento

Sistema de gerenciamento para clínica veterinária desenvolvido em **Java** com banco de dados **SQLite**, aplicando os princípios da Orientação a Objetos: **herança**, **polimorfismo** e **encapsulamento**.

---

## 📋 Funcionalidades (CRUD completo)

| Entidade      | Criar | Listar | Buscar por ID | Atualizar | Excluir |
|---------------|:-----:|:------:|:-------------:|:---------:|:-------:|
| Tutores       | ✅    | ✅     | ✅            | ✅        | ✅      |
| Veterinários  | ✅    | ✅     | ✅            | ✅        | ✅      |
| Animais       | ✅    | ✅     | ✅            | ✅        | ✅      |
| Consultas     | ✅    | ✅     | ✅            | ✅        | ✅      |

---

## 🏗️ Estrutura do Projeto

```
clinica_veterinaria/
├── pom.xml                          # Configuração Maven (dependências)
├── .gitignore
├── README.md
└── src/
    └── main/
        └── java/
            ├── models/              # Classes de domínio (OO)
            │   ├── Pessoa.java      # Classe base abstrata
            │   ├── Tutor.java       # Subclasse de Pessoa
            │   ├── Veterinario.java # Subclasse de Pessoa
            │   ├── Animal.java      # Entidade Animal
            │   └── Consulta.java    # Entidade Consulta
            ├── repositories/        # Camada de acesso a dados (SQL aqui)
            │   ├── TutorRepository.java
            │   ├── VeterinarioRepository.java
            │   ├── AnimalRepository.java
            │   └── ConsultaRepository.java
            ├── database/            # Configuração e inicialização do banco
            │   └── DatabaseConnection.java
            └── ui/                  # Interface com o usuário
                └── Menu.java        # Ponto de entrada (main)
```

---

## 🧬 Diagrama de Classes

```
                    ┌─────────────────────────┐
                    │        <<abstract>>      │
                    │          Pessoa          │
                    ├─────────────────────────┤
                    │ - id: int               │
                    │ - nome: String          │
                    │ - telefone: String      │
                    │ - email: String         │
                    ├─────────────────────────┤
                    │ + getId(): int          │
                    │ + getNome(): String     │
                    │ + setNome(String): void │
                    │ + apresentar(): String  │ ◄── método abstrato (polimorfismo)
                    └────────────┬────────────┘
                                 │  herança
              ┌──────────────────┴──────────────────┐
              │                                     │
   ┌──────────▼────────────┐           ┌────────────▼──────────┐
   │         Tutor         │           │      Veterinario       │
   ├───────────────────────┤           ├───────────────────────┤
   │ - endereco: String    │           │ - crmv: String        │
   ├───────────────────────┤           │ - especialidade: Str  │
   │ + getEndereco(): Str  │           ├───────────────────────┤
   │ + apresentar(): Str   │           │ + getCrmv(): String   │
   └───────────┬───────────┘           │ + apresentar(): Str   │
               │ possui                └──────────┬────────────┘
               │ (1..*)                           │ realiza
   ┌───────────▼───────────┐           ┌──────────▼────────────┐
   │         Animal        │           │       Consulta         │
   ├───────────────────────┤           ├───────────────────────┤
   │ - id: int             │           │ - id: int             │
   │ - nome: String        ├───────────► - animalId: int       │
   │ - especie: String     │  participa │ - veterinarioId: int  │
   │ - raca: String        │           │ - data: String        │
   │ - idadeAnos: int      │           │ - motivo: String      │
   │ - tutorId: int        │           │ - diagnostico: String │
   └───────────────────────┘           └───────────────────────┘
```

### Pilares de OO aplicados

| Pilar           | Onde é aplicado |
|-----------------|-----------------|
| **Herança**     | `Tutor` e `Veterinario` herdam de `Pessoa` |
| **Polimorfismo**| Método `apresentar()` é sobrescrito em cada subclasse com saída diferente |
| **Encapsulamento** | Todos os atributos são `private`, acessados via getters/setters com validação |

### Separação de responsabilidades

- **`models/`** — só lógica de negócio, sem SQL
- **`repositories/`** — todo SQL fica aqui (prepared statements, sem concatenação)
- **`database/`** — gerência de conexão centralizada
- **`ui/`** — interação com o usuário, sem lógica de banco

---

## ⚙️ Pré-requisitos

- Java 21+
- Maven 3.8+

Verifique com:
```bash
java -version
mvn -version
```

---

## 🚀 Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/clinica-veterinaria.git
cd clinica-veterinaria
```

### 2. Compile o projeto

```bash
mvn compile
```

### 3. Execute o sistema

```bash
mvn exec:java
```

> O banco `clinica_veterinaria.db` é criado automaticamente na primeira execução. Não é necessário instalar nenhum banco de dados.

### (Opcional) Gerar JAR executável

```bash
mvn package
java -jar target/clinica-veterinaria-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 🗃️ Esquema do Banco de Dados

```sql
CREATE TABLE tutores (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome     TEXT NOT NULL,
    telefone TEXT,
    email    TEXT,
    endereco TEXT
);

CREATE TABLE veterinarios (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    nome          TEXT NOT NULL,
    telefone      TEXT,
    email         TEXT,
    crmv          TEXT NOT NULL UNIQUE,
    especialidade TEXT
);

CREATE TABLE animais (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome     TEXT NOT NULL,
    especie  TEXT NOT NULL,
    raca     TEXT,
    idade    INTEGER,
    tutor_id INTEGER NOT NULL,
    FOREIGN KEY (tutor_id) REFERENCES tutores(id)
);

CREATE TABLE consultas (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    animal_id      INTEGER NOT NULL,
    veterinario_id INTEGER NOT NULL,
    data           TEXT NOT NULL,
    motivo         TEXT,
    diagnostico    TEXT,
    FOREIGN KEY (animal_id)      REFERENCES animais(id),
    FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id)
);
```

---

## 🔒 Boas Práticas de Segurança

- **SQL Injection**: todos os repositórios usam `PreparedStatement` com `?` — nenhuma entrada do usuário é concatenada diretamente no SQL.
- **Conexões**: cada operação abre e fecha sua própria conexão com `try-with-resources`.
- **Arquivos sensíveis**: o `.gitignore` exclui o arquivo `.db` do repositório.

---

## 👥 Divisão de Tarefas (sugestão para a dupla)

| Integrante A | Integrante B |
|---|---|
| `models/` (Pessoa, Tutor, Veterinario, Animal, Consulta) | `repositories/` (todos os 4 repositórios) |
| `database/DatabaseConnection.java` | `ui/Menu.java` |
| README inicial + diagrama de classes | README final + instruções de execução |

---

## 📚 Referências

- [Documentação JDBC SQLite (xerial)](https://github.com/xerial/sqlite-jdbc)
- [Learn Git Branching](https://learngitbranching.js.org/)
- [Draw.io — diagramas de classe](https://draw.io)
