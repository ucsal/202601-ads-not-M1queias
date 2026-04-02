package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Prova;
import java.util.List;

/**
 * ISP: interface dedicada exclusivamente ao repositório de Prova.
 * DIP: serviços dependem desta abstração, não de implementações concretas.
 */
public interface ProvaRepository {
    void salvar(Prova prova);
    List<Prova> listarTodos();
    boolean existePorId(long id);
}
