package com.projeto.sistema.repositories;

import java.util.Optional;

import com.projeto.sistema.models.User;

/**
 * Interface para operações relacionadas a repositórios de usuários.
 * Define os métodos básicos para registrar e buscar usuários.
 */
public interface UserRepositoryInterface {

    /**
     * Registra um novo usuário com email e senha hasheada.
     *
     * @param email       O email do usuário.
     * @param rawPassword A senha do usuário em texto puro (será hasheada internamente).
     */
    void createUser(String email, String rawPassword);

    /**
     * Busca um usuário pelo email.
     *
     * @param email O email do usuário.
     * @return Um {@link Optional} contendo o usuário, caso encontrado.
     */
    Optional<User> findUserByEmail(String email);
}
