package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Prova;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;

import java.util.List;

/**
 * SRP: responsável apenas pela lógica de negócio de provas.
 * DIP: depende da abstração ProvaRepository.
 */
public class ProvaService {

    private final ProvaRepository repository;

    public ProvaService(ProvaRepository repository) {
        this.repository = repository;
    }

    public Prova cadastrar(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("título inválido");
        }
        var prova = new Prova();
        prova.setTitulo(titulo);
        repository.salvar(prova);
        return prova;
    }

    public List<Prova> listarTodos() {
        return repository.listarTodos();
    }

    public boolean existePorId(long id) {
        return repository.existePorId(id);
    }
}
