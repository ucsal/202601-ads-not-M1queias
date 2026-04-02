package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;

import java.util.List;

/**
 * SRP: responsável apenas pela lógica de negócio de questões.
 * DIP: depende da abstração QuestaoRepository.
 */
public class QuestaoService {

    private final QuestaoRepository repository;

    public QuestaoService(QuestaoRepository repository) {
        this.repository = repository;
    }

    public Questao cadastrar(long provaId, String enunciado, String[] alternativas,
                             char alternativaCorreta, String fenInicial) {
        var q = new Questao();
        q.setProvaId(provaId);
        q.setEnunciado(enunciado);
        q.setAlternativas(alternativas);
        q.setAlternativaCorreta(alternativaCorreta);
        q.setFenInicial(fenInicial);
        repository.salvar(q);
        return q;
    }

    public List<Questao> listarPorProva(long provaId) {
        return repository.listarPorProva(provaId);
    }
}
