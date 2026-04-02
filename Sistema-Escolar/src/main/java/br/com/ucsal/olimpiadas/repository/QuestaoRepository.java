package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Questao;
import java.util.List;

/**
 * ISP: interface dedicada exclusivamente ao repositório de Questao.
 * DIP: serviços dependem desta abstração, não de implementações concretas.
 */
public interface QuestaoRepository {
    void salvar(Questao questao);
    List<Questao> listarPorProva(long provaId);
}
