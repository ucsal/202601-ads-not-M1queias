package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Participante;
import java.util.List;
import java.util.Optional;

/**
 * ISP: interface dedicada exclusivamente ao repositório de Participante.
 * DIP: serviços dependem desta abstração, não de implementações concretas.
 */
public interface ParticipanteRepository {
    void salvar(Participante participante);
    List<Participante> listarTodos();
    Optional<Participante> buscarPorId(long id);
    boolean existePorId(long id);
}
