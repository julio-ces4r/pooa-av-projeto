package com.projeto.sistema.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.sistema.annotations.Inject;
import com.projeto.sistema.annotations.Rota;
import com.projeto.sistema.annotations.Singleton;
import com.projeto.sistema.repositories.UserRepositoryInterface;
import com.projeto.sistema.models.User;
import com.projeto.sistema.utils.PasswordUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador responsável pelo gerenciamento de usuários, incluindo registro e autenticação.
 */

@Singleton
public class UserController {
    
    /**
     * Repositório para operações relacionadas aos usuários.
     */
	
    @Inject
    private UserRepositoryInterface userRepository;

    /**
     * Utilizado para mapear e processar JSON na requisição e resposta.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Registra um novo usuário no sistema.
     *
     * @param req  Objeto {@link HttpServletRequest} contendo a requisição HTTP.
     * @param resp Objeto {@link HttpServletResponse} para enviar a resposta HTTP.
     * @throws IOException Caso ocorra algum erro ao ler a entrada ou escrever a saída.
     */
    @Rota("/usuario")
    public void registerUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!req.getMethod().equalsIgnoreCase("POST")) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        Map<String, String> body = objectMapper.readValue(req.getInputStream(), Map.class);
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, Map.of("error", "Email e senha são obrigatórios."));
            return;
        }

        try {
            userRepository.createUser(email, password);
            sendJsonResponse(resp, Map.of("message", "Usuário registrado com sucesso!"));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            sendJsonResponse(resp, Map.of("error", e.getMessage()));
        }
    }

    /**
     * Autentica um usuário existente no sistema.
     *
     * @param req  Objeto {@link HttpServletRequest} contendo a requisição HTTP.
     * @param resp Objeto {@link HttpServletResponse} para enviar a resposta HTTP.
     * @throws IOException Caso ocorra algum erro ao ler a entrada ou escrever a saída.
     */
    @Rota("/usuario/autenticar")
    public void authenticateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!req.getMethod().equalsIgnoreCase("POST")) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            sendJsonResponse(resp, Map.of("error", "Método não permitido. Use POST."));
            return;
        }

        Map<String, String> body = objectMapper.readValue(req.getInputStream(), Map.class);
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, Map.of("error", "Email e senha são obrigatórios."));
            return;
        }

        Optional<User> userOpt = userRepository.findUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedInput = PasswordUtils.hashWithSalt(password, user.getSalt());
            if (hashedInput.equals(user.getHashedPassword())) {
                sendJsonResponse(resp, Map.of(
                    "id", user.getId(),
                    "email", user.getEmail()
                ));
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                sendJsonResponse(resp, Map.of("error", "Senha incorreta."));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            sendJsonResponse(resp, Map.of("error", "Usuário não encontrado."));
        }
    }

    /**
     * Envia uma resposta JSON com o conteúdo especificado.
     *
     * @param resp Objeto {@link HttpServletResponse} para enviar a resposta HTTP.
     * @param data Mapa contendo os dados a serem enviados na resposta JSON.
     * @throws IOException Caso ocorra algum erro ao escrever a saída.
     */
    private void sendJsonResponse(HttpServletResponse resp, Map<String, Object> data) throws IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8"); // Configura o charset para UTF-8
    objectMapper.writeValue(resp.getWriter(), data);
    }
}
