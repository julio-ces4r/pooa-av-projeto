package com.projeto.sistema.repositories;

import com.projeto.sistema.annotations.Singleton;
import com.projeto.sistema.models.User;
import com.projeto.sistema.utils.PasswordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de {@link UserRepositoryInterface} que utiliza memória para armazenar dados.
 * Esta classe é útil para testes ou quando não há necessidade de persistência em banco de dados.
 * Implementa o padrão Singleton para garantir que apenas uma instância seja utilizada.
 */

@Singleton
public class MemoryUserRepository implements UserRepositoryInterface {

    /**
     * Instância única do repositório.
     */
    private static MemoryUserRepository instance;

    /**
     * Lista de usuários armazenados em memória.
     */
    private final List<User> users = new ArrayList<>();

    /**
     * Construtor privado para evitar criação de múltiplas instâncias.
     */
    private MemoryUserRepository() {}

    /**
     * Obtém a instância única de {@link MemoryUserRepository}.
     * 
     * @return A instância única de {@link MemoryUserRepository}.
     */
    public static synchronized MemoryUserRepository getInstance() {
        if (instance == null) {
            instance = new MemoryUserRepository();
        }
        return instance;
    }

    /**
     * Registra um novo usuário na memória.
     * A senha fornecida é hasheada com um salt antes de ser armazenada.
     *
     * @param email       O email do usuário.
     * @param rawPassword A senha em texto puro.
     * @throws IllegalArgumentException Se o email já estiver registrado.
     */
    @Override
    public void createUser(String email, String rawPassword) {
        if (findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("O e-mail já está registrado.");
        }

        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashWithSalt(rawPassword, salt);

        int userId = users.size() + 1; // ID incremental
        User user = new User(userId, email, hashedPassword, salt);
        users.add(user);
    }

    /**
     * Busca um usuário em memória pelo email.
     *
     * @param email O email do usuário.
     * @return Um {@link Optional} contendo o usuário, caso encontrado.
     */
    @Override
    public Optional<User> findUserByEmail(String email) {
        return users.stream()
                    .filter(user -> user.getEmail().equals(email))
                    .findFirst();
    }
}
