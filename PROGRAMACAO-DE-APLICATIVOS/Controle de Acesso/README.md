# Sistema de Controle de Acesso Corporativo

Este projeto consiste em um sistema informatizado para gerenciar usuários, níveis de permissão e registros de entrada e saída em áreas específicas de uma organização. A solução foi desenvolvida em **Java** utilizando o paradigma de **Orientação a Objetos (POO)** e a arquitetura **MVC (Model-View-Controller)**.

## 🏛️ Decisões de Projeto e Arquitetura

O sistema foi estruturado seguindo o padrão arquitetural **MVC** para garantir a separação de responsabilidades, facilidade de manutenção e escalabilidade do código:

* **Model (`model`):** Contém as classes que representam o negócio e as entidades de dados (`Usuario`, `Cargo`, `RegistroAcesso`). Elas guardam o estado e as validações essenciais das entidades.
* **Controller (`controller`):** A classe `ControleAcessoController` centraliza as regras de negócio, a lógica das matrizes de permissão, o gerenciamento das listas em memória e o mecanismo de autenticação.
* **View (`view`):** A classe `Menu` funciona como a interface de linha de comando (CLI), sendo a única responsável por interagir diretamente com o usuário através do console.

### 🗄️ Estrutura de Dados Utilizada
Para o armazenamento e gerenciamento dos dados em tempo de execução, foi utilizada a estrutura **`java.util.ArrayList`** (implementando a interface `List`).
* **Motivação:** Como os dados não precisavam ser persistidos em um banco de dados relacional físico neste estágio, o `ArrayList` oferece acesso rápido e dinâmico indexado para busca de usuários durante o login e inserção contínua e linear para o histórico de auditoria.

### 🛡️ Modelagem de Permissões e Segurança
* **Níveis de Acesso (Enums):** Foi criado o enum `Cargo` contendo as constantes `ADMINISTRADOR`, `FUNCIONARIO` e `VISITANTE`. O uso de enumerações previne erros de digitação (ex: "admin" vs "Admin") e garante consistência de tipos em tempo de compilação.
* **Ofuscação de Senhas:** Atendendo ao requisito de segurança, as senhas não são armazenadas em texto puro. Ao criar ou alterar uma senha, a classe `Usuario` aciona o método privado `hashSenha()`, que aplica uma máscara/sufixo ao texto original. A validação do login é feita comparando o resultado mascarado, blindando a senha original de leituras diretas na memória (`getSenha`).
* **Tratamento de Exceções:** Para garantir a resiliência do sistema, todas as entradas de dados via teclado (`Scanner`) sujeitas a falhas foram envolvidas em blocos `try-catch` capturando `InputMismatchException`. Isso impede o encerramento abrupto do sistema caso o usuário digite caracteres textuais em menus numéricos.

---

## 🚀 Roteiro de Testes (Validação dos Requisitos)

Para homologação do sistema, foram executados **5 cenários críticos de teste** simulando o comportamento real da aplicação na validação de acessos e restrições:

### 🟩 Cenário 1: Tentativa de Acesso sem Autenticação (Bloqueio Prévio)
* **Ação:** Com o sistema recém-iniciado (sem nenhuma sessão ativa), o usuário seleciona a opção *2 - Tentar Acessar uma Área*.
* **Resultado Esperado:** O sistema exibe um aviso de erro informando que é obrigatório realizar o login antes de solicitar acesso. A tentativa não prossegue e nenhum log invasivo é gerado sem identificação.

### 🟨 Cenário 2: Tratamento de Falha na Autenticação (Senha Incorreta)
* **Ação:** Selecionada a opção *1 - Fazer Login*, inserido o ID válido `2` (Ana Vendas), porém digitada a senha incorreta `senhaErrada123`.
* **Resultado Esperado:** O sistema rejeita o login com a mensagem *"Falha na autenticação: ID ou Senha incorretos"*, mantendo o estado global como "Nenhum usuário logado" de forma segura.

### 🟩 Cenário 3: Funcionário em Área Permitida vs. Área Restrita
* **Ação:** Efetuado o login com o ID `2` e senha correta `ana123` (Nível: `FUNCIONARIO`).
    1. Usuário tenta acessar a área `"Recepção"`.
    2. Em seguida, tenta acessar a área `"Sala de Servidores"`.
* **Resultado Esperado:**
    1. Para a Recepção, o sistema retorna **Acesso Liberado** e abre a porta virtual.
    2. Para a Sala de Servidores, a regra de negócio barra o usuário retornando **Acesso Negado**, protegendo a infraestrutura crítica.

### 🟥 Cenário 4: Visitante em Área Proibida
* **Ação:** Efetuado o logout do usuário anterior e realizado o login com o ID `3` e senha `lucas123` (Nível: `VISITANTE`). O usuário tenta acessar a área `"Almoxarifado"`.
* **Resultado Esperado:** O sistema aplica a restrição máxima de visitantes e retorna **Acesso Negado**. (Visitantes possuem permissão exclusiva para a área da `"Recepção"`).

### 🟦 Cenário 5: Auditoria do Histórico Completo pelo Administrador
* **Ação:** Realizado o login com o ID `1` e senha `admin123` (Nível: `ADMINISTRADOR`). Selecionada a opção *4 - Consultar Histórico Completo*.
* **Resultado Esperado:** O sistema exibe com sucesso a lista cronológica contendo os carimbos de data/hora exatos (`LocalDateTime`), o nome do funcionário, a área que ele tentou acessar e o respectivo desfecho (se foi AUTORIZADO ou NEGADO) coletados nos cenários 3 e 4.
*(Nota: Se um usuário de nível FUNCIONARIO tentar acessar essa opção, o sistema bloqueia a listagem informando falta de privilégios).*

---

## 🔮 Possíveis Melhorias Futuras

Embora o sistema cumpra com excelência os requisitos de POO e resiliência exigidos, mapeamos os seguintes pontos para evolução da arquitetura em versões futuras:

1.  **Persistência Real de Dados:** Substituir as listas em memória (`ArrayList`) pela integração com um banco de dados relacional (ex: PostgreSQL ou MySQL) via JDBC ou Spring Data JPA.
2.  **Criptografia Forte:** Evoluir o método de ofuscação simples de strings para algoritmos reais de Hash de via única e salt robusto, como o **BCrypt** ou **PBKDF2**.
3.  **Interface Gráfica (GUI):** Migrar a interface de console para uma aplicação web com Spring Boot e Thymeleaf/React ou desktop via JavaFX.