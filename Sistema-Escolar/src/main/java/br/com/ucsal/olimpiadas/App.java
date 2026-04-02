package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.repository.RepositoryFactory;
import br.com.ucsal.olimpiadas.seed.DataSeeder;
import br.com.ucsal.olimpiadas.service.*;
import br.com.ucsal.olimpiadas.ui.ConsoleUI;
import br.com.ucsal.olimpiadas.ui.FenRenderer;

import java.util.Scanner;

/**
 * Composition Root — único ponto onde implementações concretas são instanciadas.
 * DIP: toda dependência flui para dentro via construtor; nenhuma classe filha usa "new".
 */
public class App {

    public static void main(String[] args) {

        // ── Repositórios (implementações concretas, via factory) ──────────────
        var alunoRepo      = RepositoryFactory.criarParticipanteRepository();
        var disciplinaRepo = RepositoryFactory.criarProvaRepository();
        var questaoRepo    = RepositoryFactory.criarQuestaoRepository();
        var avaliacaoRepo  = RepositoryFactory.criarTentativaRepository();

        // ── Serviços ──────────────────────────────────────────────────────────
        var alunoService      = new ParticipanteService(alunoRepo);
        var disciplinaService = new ProvaService(disciplinaRepo);
        var questaoService    = new QuestaoService(questaoRepo);
        var notaCalculator    = new NotaCalculator();
        var avaliacaoService  = new TentativaService(avaliacaoRepo, notaCalculator);

        // ── Dados de exemplo ──────────────────────────────────────────────────
        new DataSeeder(disciplinaService, questaoService).executar();

        // ── Interface de console ──────────────────────────────────────────────
        var renderer = new FenRenderer();
        var ui = new ConsoleUI(
                alunoService,
                disciplinaService,
                questaoService,
                avaliacaoService,
                renderer,
                new Scanner(System.in)
        );

        ui.iniciar();
    }
}
