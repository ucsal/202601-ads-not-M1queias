package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.service.ParticipanteService;
import br.com.ucsal.olimpiadas.service.ProvaService;
import br.com.ucsal.olimpiadas.service.QuestaoService;
import br.com.ucsal.olimpiadas.service.TentativaService;

import java.util.Scanner;

/**
 * SRP: responsável apenas por ler entradas do usuário e exibir saídas no console.
 *      Nenhuma lógica de negócio aqui.
 * DIP: recebe todos os serviços via construtor (abstrações, não implementações).
 */
public class ConsoleUI {

    private final ParticipanteService participanteService;
    private final ProvaService provaService;
    private final QuestaoService questaoService;
    private final TentativaService tentativaService;
    private final FenRenderer fenRenderer;
    private final Scanner in;

    public ConsoleUI(ParticipanteService participanteService,
                     ProvaService provaService,
                     QuestaoService questaoService,
                     TentativaService tentativaService,
                     FenRenderer fenRenderer,
                     Scanner in) {
        this.participanteService = participanteService;
        this.provaService = provaService;
        this.questaoService = questaoService;
        this.tentativaService = tentativaService;
        this.fenRenderer = fenRenderer;
        this.in = in;
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    SISTEMA DE AVALIAÇÃO ESCOLAR      ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("1) Matricular aluno");
            System.out.println("2) Cadastrar disciplina");
            System.out.println("3) Cadastrar questão (A–E) em uma disciplina");
            System.out.println("4) Aplicar avaliação (selecionar aluno + disciplina)");
            System.out.println("5) Consultar histórico de avaliações");
            System.out.println("0) Encerrar sistema");
            System.out.print("\n> ");

            switch (in.nextLine()) {
                case "1" -> matricularAluno();
                case "2" -> cadastrarDisciplina();
                case "3" -> cadastrarQuestao();
                case "4" -> aplicarAvaliacao();
                case "5" -> consultarHistorico();
                case "0" -> {
                    System.out.println("\nSistema encerrado. Até logo!");
                    return;
                }
                default -> System.out.println("[!] Opção inválida. Tente novamente.");
            }
        }
    }

    // ── Aluno ────────────────────────────────────────────────────────────────

    private void matricularAluno() {
        System.out.println("\n--- Matrícula de Aluno ---");
        System.out.print("Nome completo: ");
        var nome = in.nextLine();

        System.out.print("E-mail institucional (opcional): ");
        var email = in.nextLine();

        try {
            var p = participanteService.cadastrar(nome, email);
            System.out.println("[✓] Aluno matriculado com sucesso. Matrícula: " + p.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Erro ao matricular: " + e.getMessage());
        }
    }

    // ── Disciplina ────────────────────────────────────────────────────────────

    private void cadastrarDisciplina() {
        System.out.println("\n--- Cadastro de Disciplina ---");
        System.out.print("Nome da disciplina: ");
        var titulo = in.nextLine();

        try {
            var prova = provaService.cadastrar(titulo);
            System.out.println("[✓] Disciplina cadastrada. Código: " + prova.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Erro ao cadastrar: " + e.getMessage());
        }
    }

    // ── Questão ───────────────────────────────────────────────────────────────

    private void cadastrarQuestao() {
        if (provaService.listarTodos().isEmpty()) {
            System.out.println("[!] Nenhuma disciplina cadastrada. Cadastre uma primeiro.");
            return;
        }

        var provaId = escolherDisciplina();
        if (provaId == null) return;

        System.out.println("\nEnunciado da questão:");
        var enunciado = in.nextLine();

        var alternativas = new String[5];
        for (int i = 0; i < 5; i++) {
            char letra = (char) ('A' + i);
            System.out.print("Alternativa " + letra + ": ");
            alternativas[i] = letra + ") " + in.nextLine();
        }

        System.out.print("Gabarito — alternativa correta (A–E): ");
        char correta;
        try {
            correta = Questao.normalizar(in.nextLine().trim().charAt(0));
        } catch (Exception e) {
            System.out.println("[!] Alternativa inválida.");
            return;
        }

        var q = questaoService.cadastrar(provaId, enunciado, alternativas, correta, null);
        System.out.println("[✓] Questão nº " + q.getId() + " adicionada à disciplina " + provaId + ".");
    }

    // ── Aplicar Avaliação ─────────────────────────────────────────────────────

    private void aplicarAvaliacao() {
        if (participanteService.listarTodos().isEmpty()) {
            System.out.println("[!] Nenhum aluno matriculado. Matricule alunos primeiro.");
            return;
        }
        if (provaService.listarTodos().isEmpty()) {
            System.out.println("[!] Nenhuma disciplina cadastrada. Cadastre uma primeiro.");
            return;
        }

        var participanteId = escolherAluno();
        if (participanteId == null) return;

        var provaId = escolherDisciplina();
        if (provaId == null) return;

        var questoesDaProva = questaoService.listarPorProva(provaId);
        if (questoesDaProva.isEmpty()) {
            System.out.println("[!] Esta disciplina ainda não possui questões cadastradas.");
            return;
        }

        Tentativa tentativa = tentativaService.iniciar(participanteId, provaId);

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         INÍCIO DA AVALIAÇÃO          ║");
        System.out.println("╚══════════════════════════════════════╝");

        for (var q : questoesDaProva) {
            System.out.println("\nQuestão " + q.getId() + ":");
            System.out.println(q.getEnunciado());

            System.out.println("Diagrama de referência:");
            fenRenderer.renderizar(q.getFenInicial());

            for (var alt : q.getAlternativas()) {
                System.out.println("  " + alt);
            }

            System.out.print("Sua resposta (A–E): ");
            char marcada;
            try {
                marcada = Questao.normalizar(in.nextLine().trim().charAt(0));
            } catch (Exception e) {
                System.out.println("[!] Resposta inválida — questão será computada como errada.");
                marcada = 'X';
            }

            tentativaService.registrarResposta(tentativa, q, marcada);
        }

        int nota = tentativaService.finalizar(tentativa);
        int total = tentativa.getRespostas().size();
        double percentual = (nota * 10.0) / total;

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          RESULTADO DA AVALIAÇÃO      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.printf("  Acertos  : %d / %d%n", nota, total);
        System.out.printf("  Nota     : %.1f%n", percentual);
        System.out.println(percentual >= 6.0 ? "  Situação : ✓ APROVADO" : "  Situação : ✗ REPROVADO");
        System.out.println("══════════════════════════════════════════");
    }

    // ── Histórico ─────────────────────────────────────────────────────────────

    private void consultarHistorico() {
        var tentativas = tentativaService.listarTodos();
        if (tentativas.isEmpty()) {
            System.out.println("[!] Nenhuma avaliação realizada ainda.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       HISTÓRICO DE AVALIAÇÕES        ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.printf("  %-5s %-12s %-12s %-10s %-8s%n",
                "ID", "Matrícula", "Disciplina", "Nota", "Situação");
        System.out.println("  " + "─".repeat(52));

        for (var t : tentativas) {
            int acertos = tentativaService.calcularNota(t);
            int total   = t.getRespostas().size();
            double nota = total > 0 ? (acertos * 10.0) / total : 0;
            String situacao = nota >= 6.0 ? "Aprovado" : "Reprovado";

            System.out.printf("  %-5d %-12d %-12d %-10.1f %-8s%n",
                    t.getId(), t.getParticipanteId(), t.getProvaId(), nota, situacao);
        }
        System.out.println("══════════════════════════════════════════");
    }

    // ── Helpers de seleção ────────────────────────────────────────────────────

    private Long escolherAluno() {
        System.out.println("\nAlunos matriculados:");
        for (var p : participanteService.listarTodos()) {
            System.out.printf("  [%d] %s%n", p.getId(), p.getNome());
        }
        System.out.print("Informe a matrícula do aluno: ");

        try {
            long id = Long.parseLong(in.nextLine());
            if (!participanteService.existePorId(id)) {
                System.out.println("[!] Matrícula não encontrada.");
                return null;
            }
            return id;
        } catch (Exception e) {
            System.out.println("[!] Entrada inválida.");
            return null;
        }
    }

    private Long escolherDisciplina() {
        System.out.println("\nDisciplinas disponíveis:");
        for (var p : provaService.listarTodos()) {
            System.out.printf("  [%d] %s%n", p.getId(), p.getTitulo());
        }
        System.out.print("Informe o código da disciplina: ");

        try {
            long id = Long.parseLong(in.nextLine());
            if (!provaService.existePorId(id)) {
                System.out.println("[!] Disciplina não encontrada.");
                return null;
            }
            return id;
        } catch (Exception e) {
            System.out.println("[!] Entrada inválida.");
            return null;
        }
    }
}
