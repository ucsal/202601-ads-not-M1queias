package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.model.Resposta;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;

import java.util.List;

/**
 * SRP: responsável apenas pela lógica de aplicação de provas e registro de tentativas.
 * DIP: depende das abstrações TentativaRepository e NotaCalculator.
 */
public class TentativaService {

    private final TentativaRepository repository;
    private final NotaCalculator notaCalculator;

    public TentativaService(TentativaRepository repository, NotaCalculator notaCalculator) {
        this.repository = repository;
        this.notaCalculator = notaCalculator;
    }

    public Tentativa iniciar(long participanteId, long provaId) {
        var t = new Tentativa();
        t.setParticipanteId(participanteId);
        t.setProvaId(provaId);
        return t;
    }

    public void registrarResposta(Tentativa tentativa, Questao questao, char alternativaMarcada) {
        var r = new Resposta();
        r.setQuestaoId(questao.getId());
        r.setAlternativaMarcada(alternativaMarcada);
        r.setCorreta(questao.isRespostaCorreta(alternativaMarcada));
        tentativa.getRespostas().add(r);
    }

    public int finalizar(Tentativa tentativa) {
        repository.salvar(tentativa);
        return notaCalculator.calcular(tentativa);
    }

    public List<Tentativa> listarTodos() {
        return repository.listarTodos();
    }

    public int calcularNota(Tentativa tentativa) {
        return notaCalculator.calcular(tentativa);
    }
}
