package br.com.ucsal.olimpiadas.model;

import java.util.ArrayList;
import java.util.List;

// SRP: esta classe representa apenas os dados de uma tentativa.
public class Tentativa {
    private long id;
    private long participanteId;
    private long provaId;
    private final List<Resposta> respostas = new ArrayList<>();

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getParticipanteId() { return participanteId; }
    public void setParticipanteId(long participanteId) { this.participanteId = participanteId; }

    public long getProvaId() { return provaId; }
    public void setProvaId(long provaId) { this.provaId = provaId; }

    public List<Resposta> getRespostas() { return respostas; }
}
