package com.projeto.sistema.servlets;

import com.projeto.sistema.annotations.Inject;
import com.projeto.sistema.annotations.Rota;
import com.projeto.sistema.utils.DependencyManager;
import com.projeto.sistema.commands.Command;
import com.projeto.sistema.commands.RouteCommand;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Servlet principal responsável pelo roteamento e injeção de dependências.
 */
public class MainServlet extends HttpServlet {

    private final Map<String, Command> rotaMap = new HashMap<>();
    private final DependencyManager dependencyManager = new DependencyManager();

    @Override
    public void init() throws ServletException {
        try {
            scanForRoutesAndDependencies("com.projeto.sistema"); // Indique o pacote para escanear
        } catch (Exception e) {
            throw new ServletException("Erro ao inicializar rotas", e);
        }
    }

    private void scanForRoutesAndDependencies(String basePackage) throws Exception {
        Reflections reflections = new Reflections(
                basePackage,
                Scanners.MethodsAnnotated,
                Scanners.FieldsAnnotated,
                Scanners.TypesAnnotated
        );

        // Detecta e registra rotas
        Set<Method> methodsWithRoutes = reflections.getMethodsAnnotatedWith(Rota.class);
        for (Method method : methodsWithRoutes) {
            Class<?> controllerClass = method.getDeclaringClass();
            Object controllerInstance = dependencyManager.getOrCreateInstance(controllerClass);

            Rota rota = method.getAnnotation(Rota.class);
            if (rotaMap.containsKey(rota.value())) {
                throw new IllegalStateException("Rota duplicada detectada: " + rota.value());
            }

            // Adiciona um RouteCommand ao mapa de rotas
            rotaMap.put(rota.value(), new RouteCommand(controllerInstance, method));
        }

        // Injeta dependências nos campos anotados
        Set<Field> fieldsWithInject = reflections.getFieldsAnnotatedWith(Inject.class);
        dependencyManager.injectDependencies(fieldsWithInject);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        Command command = rotaMap.get(path);

        if (command == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota não encontrada");
            return;
        }

        try {
            command.execute(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro ao processar rota", e);
        }
    }
}
