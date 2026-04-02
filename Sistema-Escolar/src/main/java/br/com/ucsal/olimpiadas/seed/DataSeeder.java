package br.com.ucsal.olimpiadas.seed;

import br.com.ucsal.olimpiadas.service.ProvaService;
import br.com.ucsal.olimpiadas.service.QuestaoService;

/**
 * SRP: responsabilidade única — popular o sistema com dados iniciais de exemplo.
 * DIP: recebe os serviços via construtor.
 */
public class DataSeeder {

    private final ProvaService provaService;
    private final QuestaoService questaoService;

    public DataSeeder(ProvaService provaService, QuestaoService questaoService) {
        this.provaService = provaService;
        this.questaoService = questaoService;
    }

    public void executar() {
        var disciplina = provaService.cadastrar("Lógica de Programação — Avaliação 1");

        questaoService.cadastrar(
            disciplina.getId(),
            """
            Questão 1 — Análise de Algoritmo.
            Considere o trecho de código abaixo.
            Qual é a saída produzida quando n = 8?
            """,
            new String[]{
                "A) 2",
                "B) 4",
                "C) 8",
                "D) 16",
                "E) 64"
            },
            'B',
            null
        );
    }
}
