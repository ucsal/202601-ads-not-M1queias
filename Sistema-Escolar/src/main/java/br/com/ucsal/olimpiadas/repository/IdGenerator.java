package br.com.ucsal.olimpiadas.repository;

/**
 * ISP: interface mínima para geração de IDs sequenciais.
 * DIP: serviços e repositórios dependem desta abstração.
 */
public interface IdGenerator {
    long proximo();
}
