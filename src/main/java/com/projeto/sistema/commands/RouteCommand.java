package com.projeto.sistema.commands;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Comando que adapta um método controlador para o padrão Command.
 */
public class RouteCommand implements Command {

    private final Object controllerInstance;
    private final Method method;

    /**
     * Cria uma nova instância de {@link RouteCommand}.
     *
     * @param controllerInstance A instância do controlador onde o método está definido.
     * @param method             O método a ser invocado.
     */
    public RouteCommand(Object controllerInstance, Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        method.invoke(controllerInstance, req, resp);
    }
}
