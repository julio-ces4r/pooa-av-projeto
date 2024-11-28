package com.projeto.sistema.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface que define um comando no padrão Command.
 */
public interface Command {
    /**
     * Executa o comando associado a uma rota.
     *
     * @param req  Objeto {@link HttpServletRequest} contendo a requisição HTTP.
     * @param resp Objeto {@link HttpServletResponse} para enviar a resposta.
     * @throws Exception Caso ocorra algum erro durante a execução.
     */
    void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
