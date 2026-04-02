package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Tentativa;
import java.util.List;

/**
 * ISP: interface dedicada exclusivamente ao repositório de Tentativa.
 * DIP: serviços dependem desta abstração, não de implementações concretas.
 */
public interface TentativaRepository {
    void salvar(Tentativa tentativa);
    List<Tentativa> listarTodos();
}
