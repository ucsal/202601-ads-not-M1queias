# Sistema de Avaliação Escolar — Refatoração SOLID

## Visão Geral

Este repositório contém a refatoração do sistema legado **Avaliação Escolar** com aplicação dos cinco princípios SOLID. O comportamento funcional original foi **preservado integralmente**: o menu, as validações, a lógica de pontuação e a renderização de diagramas funcionam exatamente como antes.

---

## Domínio do Sistema

| Conceito legado | Novo conceito        | Descrição                                      |
|-----------------|----------------------|------------------------------------------------|
| Participante    | **Aluno**            | Pessoa matriculada que realiza avaliações      |
| Prova           | **Disciplina**       | Conjunto de questões de uma matéria            |
| Questão         | **Questão**          | Item com enunciado, alternativas e gabarito    |
| Tentativa       | **Avaliação**        | Registro das respostas de um aluno             |
| Nota (acertos)  | **Nota 0–10**        | Calculada proporcionalmente aos acertos        |

---

## Estrutura de Pacotes

```
br.com.ucsal.olimpiadas
├── model/            → entidades de domínio (dados puros)
├── repository/       → interfaces e implementações in-memory
├── service/          → lógica de negócio
├── ui/               → entrada/saída do console
├── seed/             → carga inicial de dados de exemplo
└── App.java          → Composition Root (main)
```

---

## Onde cada princípio SOLID foi aplicado

### S — Single Responsibility Principle (SRP)

**Problema original:** `App.java` acumulava todas as responsabilidades: menu interativo, validação de entrada, persistência em listas estáticas, cálculo de nota e seed de dados.

**Solução aplicada:**

| Classe               | Responsabilidade única                             |
|----------------------|----------------------------------------------------|
| `model/*`            | Representar dados de domínio                       |
| `FenRenderer`        | Renderizar diagramas de referência das questões    |
| `NotaCalculator`     | Calcular a nota proporcional de uma avaliação      |
| `DataSeeder`         | Popular o sistema com disciplinas e questões demo  |
| `ConsoleUI`          | Ler entradas e exibir saídas no console            |
| `ParticipanteService`| Lógica de negócio de matrículas de alunos          |
| `ProvaService`       | Lógica de negócio de disciplinas                   |
| `QuestaoService`     | Lógica de negócio de questões                      |
| `TentativaService`   | Lógica de aplicação de avaliações e registro       |

---

### O — Open/Closed Principle (OCP)

**Problema original:** Para adicionar um novo tipo de persistência (ex.: banco de dados) seria necessário modificar a própria `App` e reescrever as listas estáticas.

**Solução aplicada:**

- `RepositoryFactory` centraliza a criação dos repositórios. Para trocar a implementação de memória por uma com banco de dados, basta criar uma nova classe que implemente a interface (ex.: `JdbcDisciplinaRepository`) e alterar a factory — **nenhum serviço é tocado**.
- `FenRenderer` pode ser substituído por uma versão com símbolos gráficos sem alterar `ConsoleUI`.

---

### L — Liskov Substitution Principle (LSP)

**Problema original:** Sem interfaces nem herança, não havia como substituir colaboradores.

**Solução aplicada:**

Cada repositório in-memory implementa fielmente o contrato de sua interface correspondente. Qualquer serviço que receba `ProvaRepository` funciona corretamente com qualquer implementação concreta — in-memory, JDBC ou arquivo — **sem alterar o serviço**.

```java
// ProvaService funciona com qualquer implementação de ProvaRepository
ProvaRepository repo = new InMemoryProvaRepository(new SequentialIdGenerator());
// amanhã: ProvaRepository repo = new JdbcProvaRepository(dataSource);
var service = new ProvaService(repo); // nada muda aqui
```

---

### I — Interface Segregation Principle (ISP)

**Problema original:** Não havia interfaces; quem precisasse listar alunos tinha acesso direto a todas as listas estáticas de `App`.

**Solução aplicada:**

Foram criadas interfaces pequenas e específicas, cada uma com apenas os métodos que seu cliente precisa:

```java
public interface ParticipanteRepository {   // Alunos
    void salvar(Participante participante);
    List<Participante> listarTodos();
    Optional<Participante> buscarPorId(long id);
    boolean existePorId(long id);
}

public interface ProvaRepository {          // Disciplinas
    void salvar(Prova prova);
    List<Prova> listarTodos();
    boolean existePorId(long id);
}

public interface IdGenerator {
    long proximo();
}
```

Nenhuma interface mistura responsabilidades de entidades diferentes.

---

### D — Dependency Inversion Principle (DIP)

**Problema original:** `App` gerenciava `ArrayList` estáticos diretamente — dependência de implementações concretas no nível mais alto do sistema.

**Solução aplicada:**

- Todos os serviços recebem suas dependências via **construtor** (interfaces, não implementações).
- `App.main` é o único ponto do sistema — a **Composition Root** — onde as implementações concretas são instanciadas e conectadas.

```java
// App.main — único lugar com "new" de dependências concretas
var disciplinaRepo    = RepositoryFactory.criarProvaRepository();
var disciplinaService = new ProvaService(disciplinaRepo);   // recebe abstração
var ui                = new ConsoleUI(disciplinaService, ...); // recebe abstração
```

Nenhuma outra classe do sistema usa `new` para criar colaboradores.

---

## Sequência de Commits Recomendada

```
1. refactor: extrair entidades de domínio para pacote model (SRP)
2. refactor: criar interfaces de repositório para Aluno, Disciplina e Avaliação (ISP + DIP)
3. refactor: implementar repositórios in-memory substituíveis (LSP + OCP)
4. refactor: extrair NotaCalculator e serviços com injeção de dependência (SRP + DIP)
5. refactor: extrair FenRenderer do App — renderização de diagramas isolada (SRP)
6. refactor: extrair ConsoleUI do App — UI desacoplada dos serviços (SRP + DIP)
7. refactor: extrair DataSeeder — carga de dados demo separada da inicialização (SRP)
8. refactor: reduzir App.main a pura Composition Root (DIP)
```

---

## Funcionalidades do Menu

```
╔══════════════════════════════════════╗
║    SISTEMA DE AVALIAÇÃO ESCOLAR      ║
╚══════════════════════════════════════╝
1) Matricular aluno
2) Cadastrar disciplina
3) Cadastrar questão (A–E) em uma disciplina
4) Aplicar avaliação (selecionar aluno + disciplina)
5) Consultar histórico de avaliações
0) Encerrar sistema
```

A nota é calculada de **0 a 10** proporcionalmente aos acertos:

```
nota = (acertos / total_questoes) × 10
```

Alunos com nota ≥ 6,0 são exibidos como **APROVADO**; abaixo disso, **REPROVADO**.

---

## Como executar

```bash
./mvnw compile exec:java -Dexec.mainClass="br.com.ucsal.olimpiadas.App"
```

ou

```bash
./mvnw package
java -jar target/*.jar
```

---

## Restrições respeitadas

- Nenhuma lógica de negócio original foi alterada
- Nenhuma funcionalidade foi removida
- Nenhum framework externo foi adicionado (sem Spring, sem Guice)
- O `pom.xml` é idêntico ao original
