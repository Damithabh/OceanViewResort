package dao;

import java.util.List;

/**
 * Generic Base DAO Interface
 * // Learned from https://www.baeldung.com/java-dao-pattern
 * Decouples business logic from persistence framework.
 */
public interface GenericDAO<T> {

    T findById(int id);

    List<T> findAll();

    boolean create(T entity);

    boolean update(T entity);

    boolean delete(int id);
}
