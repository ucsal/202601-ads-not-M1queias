package br.com.ucsal.olimpiadas.repository;

/**
 * OCP / DIP: fábrica que centraliza a criação dos repositórios.
 * Para trocar a implementação (ex.: banco de dados), basta criar outra factory
 * sem modificar os serviços ou a UI.
 */
public class RepositoryFactory {

    public static ParticipanteRepository criarParticipanteRepository() {
        return new InMemoryParticipanteRepository(new SequentialIdGenerator());
    }

    public static ProvaRepository criarProvaRepository() {
        return new InMemoryProvaRepository(new SequentialIdGenerator());
    }

    public static QuestaoRepository criarQuestaoRepository() {
        return new InMemoryQuestaoRepository(new SequentialIdGenerator());
    }

    public static TentativaRepository criarTentativaRepository() {
        return new InMemoryTentativaRepository(new SequentialIdGenerator());
    }
}
