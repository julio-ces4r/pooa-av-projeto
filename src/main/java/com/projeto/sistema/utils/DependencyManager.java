package com.projeto.sistema.utils;

import com.projeto.sistema.annotations.Inject;
import com.projeto.sistema.annotations.Singleton;
import com.projeto.sistema.factories.UserRepositoryFactory;
import com.projeto.sistema.repositories.UserRepositoryInterface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Gerenciador de dependências responsável por criar e injetar instâncias.
 */
public class DependencyManager {

    /**
     * Mapa para armazenar instâncias únicas (Singletons).
     */
    private final Map<String, Object> instanceCache = new HashMap<>();

    /**
     * Resolve e retorna a dependência para o tipo especificado.
     *
     * @param type A classe ou interface da dependência.
     * @return A instância correspondente.
     * @throws Exception Caso ocorra um erro durante a resolução.
     */
    public Object resolveDependency(Class<?> type) throws Exception {
        // Caso a classe seja anotada como Singleton, reutiliza a instância
        if (type.isAnnotationPresent(Singleton.class)) {
            return getOrCreateInstance(type);
        }

        // Caso a dependência seja o repositório de usuários
        if (type == UserRepositoryInterface.class) {
            return UserRepositoryFactory.createRepository();
        }

        // Caso o tipo seja uma interface genérica, lança uma exceção
        if (type.isInterface()) {
            throw new IllegalArgumentException("Não é possível resolver dependência para a interface: " + type.getName());
        }

        // Cria uma nova instância para classes concretas
        return type.getDeclaredConstructor().newInstance();
    }


    /**
     * Obtém ou cria uma instância para a classe especificada.
     * Respeita a anotação {@link Singleton} para reutilizar instâncias.
     *
     * @param clazz A classe para a qual criar ou obter a instância.
     * @return A instância da classe.
     * @throws Exception Caso ocorra um erro durante a criação.
     */
    public Object getOrCreateInstance(Class<?> clazz) throws Exception {
        // Verifica se é uma interface, delega ao resolveDependency
        if (clazz.isInterface()) {
            return resolveDependency(clazz);
        }

        // Retorna uma instância já existente, se disponível
        if (instanceCache.containsKey(clazz.getName())) {
            return instanceCache.get(clazz.getName());
        }

        // Cria uma nova instância
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // Se a classe for anotada com @Singleton, salva no cache
        if (clazz.isAnnotationPresent(Singleton.class)) {
            instanceCache.put(clazz.getName(), instance);
        }

        return instance;
    }


    /**
     * Injeta dependências em campos anotados com {@link Inject}.
     *
     * @param fields Os campos anotados para injeção.
     * @throws Exception Caso ocorra um erro durante a injeção.
     */
    public void injectDependencies(Set<Field> fields) throws Exception {
        for (Field field : fields) {
            Class<?> declaringClass = field.getDeclaringClass();
            Object instance = getOrCreateInstance(declaringClass);
            
            System.out.println(instance);

            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            Object dependency = resolveDependency(fieldType);
            
            System.out.println(fieldType);

            System.out.println(dependency);
            
            if (dependency == null) {
                throw new IllegalStateException("Não foi possível resolver dependência para o campo: " 
                                                + field.getName() + " em " + declaringClass.getName());
            }

            field.set(instance, dependency);
        }
    }

}
