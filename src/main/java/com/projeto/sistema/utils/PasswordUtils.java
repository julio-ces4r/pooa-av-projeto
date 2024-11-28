package com.projeto.sistema.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    /**
     * Gera um salt aleatório para ser usado no hashing de senhas.
     * 
     * @return Uma string representando o salt.
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashea a senha combinada com o salt fornecido usando SHA-256.
     * 
     * @param password A senha original.
     * @param salt     O salt associado.
     * @return O hash da senha com o salt.
     */
    public static String hashWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] hash = md.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao hashear senha", e);
        }
    }
}
