package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Participante;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;

import java.util.List;
import java.util.Optional;

/**
 * SRP: responsável apenas pela lógica de negócio de participantes.
 * DIP: recebe ParticipanteRepository (abstração) via construtor — não instancia nada.
 */
public class ParticipanteService {

    private final ParticipanteRepository repository;

    public ParticipanteService(ParticipanteRepository repository) {
        this.repository = repository;
    }

    public Participante cadastrar(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome inválido");
        }
        var p = new Participante();
        p.setNome(nome);
        p.setEmail(email);
        repository.salvar(p);
        return p;
    }

    public List<Participante> listarTodos() {
        return repository.listarTodos();
    }

    public Optional<Participante> buscarPorId(long id) {
        return repository.buscarPorId(id);
    }

    public boolean existePorId(long id) {
        return repository.existePorId(id);
    }
}
