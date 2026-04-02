package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Tentativa;

/**
 * SRP: responsabilidade única — calcular a nota de uma tentativa.
 * OCP: se a fórmula de pontuação mudar, só esta classe precisa mudar.
 */
public class NotaCalculator {

    public int calcular(Tentativa tentativa) {
        int acertos = 0;
        for (var r : tentativa.getRespostas()) {
            if (r.isCorreta()) acertos++;
        }
        return acertos;
    }
}
