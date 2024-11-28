package com.projeto.sistema.factories;

import com.projeto.sistema.repositories.HSQLDBUserRepository;
import com.projeto.sistema.repositories.MemoryUserRepository;
import com.projeto.sistema.repositories.UserRepositoryInterface;

/**
 * Fábrica para criar instâncias de {@link UserRepositoryInterface}.
 * Permite selecionar o tipo de repositório a ser utilizado com base em uma configuração.
 */
public class UserRepositoryFactory {
    /**
     * Tipo de persistência configurado para o repositório.
     */
    private static final String PERSISTENCE_TYPE = "MEMORY";

    /**
     * Cria uma instância de {@link UserRepositoryInterface} com base no tipo de persistência configurado.
     *
     * @return Uma instância de {@link UserRepositoryInterface}.
     */
    public static UserRepositoryInterface createRepository() {
        switch (PERSISTENCE_TYPE) {
            case "HSQLDB":
                return HSQLDBUserRepository.getInstance();
            case "MEMORY":
            default:
                return MemoryUserRepository.getInstance();
        }
    }
}
