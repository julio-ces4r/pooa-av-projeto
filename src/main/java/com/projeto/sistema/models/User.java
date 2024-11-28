package com.projeto.sistema.models;

/**
 * Representa um usuário no sistema.
 * Contém informações essenciais, como ID, email, senha hasheada e o salt usado para hashing.
 */
public class User {

    /**
     * Identificador único do usuário.
     */
    private final int id;

    /**
     * Email do usuário, utilizado para autenticação.
     */
    private final String email;

    /**
     * Senha do usuário em formato hasheado.
     */
    private final String hashedPassword;

    /**
     * Salt utilizado para hashear a senha do usuário.
     */
    private final String salt;

    /**
     * Construtor para inicializar um objeto {@link User}.
     *
     * @param id             Identificador único do usuário.
     * @param email          Email do usuário.
     * @param hashedPassword Senha do usuário em formato hasheado.
     * @param salt           Salt utilizado para o hashing da senha.
     */
    public User(int id, String email, String hashedPassword, String salt) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    /**
     * Retorna o identificador único do usuário.
     *
     * @return ID do usuário.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o email do usuário.
     *
     * @return Email do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna a senha do usuário em formato hasheado.
     *
     * @return Senha hasheada do usuário.
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Retorna o salt utilizado para o hashing da senha.
     *
     * @return Salt da senha do usuário.
     */
    public String getSalt() {
        return salt;
    }
}
